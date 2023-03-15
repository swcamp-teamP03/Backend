package com.example.swcamp_p03.campaign.service;

import com.example.swcamp_p03.campaign.dto.api.NaverApiMessage;
import com.example.swcamp_p03.campaign.dto.api.NaverApiMessageResultDto;
import com.example.swcamp_p03.campaign.dto.api.NaverApiRequestDto;
import com.example.swcamp_p03.campaign.dto.request.CampaignRequestDto;
import com.example.swcamp_p03.campaign.dto.response.SendMessageElementDto;
import com.example.swcamp_p03.campaign.dto.response.SendMessageResponseDto;
import com.example.swcamp_p03.campaign.entity.Campaign;
import com.example.swcamp_p03.campaign.entity.CampaignMessage;
import com.example.swcamp_p03.campaign.entity.SendMessages;
import com.example.swcamp_p03.campaign.repository.CampaignMessageRepository;
import com.example.swcamp_p03.campaign.repository.CampaignRepository;
import com.example.swcamp_p03.campaign.repository.SendMessagesRepository;
import com.example.swcamp_p03.common.exception.ErrorCode;
import com.example.swcamp_p03.common.exception.GlobalException;
import com.example.swcamp_p03.copyGroup.entity.CopyGroup;
import com.example.swcamp_p03.copyGroup.repository.CopyGroupRepository;
import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import com.example.swcamp_p03.customerGroup.entity.ExcelData;
import com.example.swcamp_p03.customerGroup.entity.ExcelFile;
import com.example.swcamp_p03.customerGroup.repository.CustomerGroupRepository;
import com.example.swcamp_p03.customerGroup.repository.ExcelDataRepository;
import com.example.swcamp_p03.user.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.commons.codec.binary.Base64;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CampaignService {

    private final MessageSource messageSource;
    private final CustomerGroupRepository customerGroupRepository;
    private final ObjectMapper mapper;
    private final CampaignRepository campaignRepository;
    private final CopyGroupRepository copyGroupRepository;
    private final CampaignMessageRepository campaignMessageRepository;
    private final SendMessagesRepository sendMessagesRepository;
    private final ExcelDataRepository excelDataRepository;

    @Transactional
    public String visitUrl(String uniqueUrl){
        log.info("uniqueUrl : {}", uniqueUrl);
        SendMessages sendMessages = sendMessagesRepository.findByUniqueUrl(uniqueUrl).orElse(null);
        log.info("sendMessages = {}", sendMessages);
        if(sendMessages == null){
            return messageSource.getMessage("frontUrl", null, null);
        }
        sendMessages.visitUrl();
        return sendMessages.getCampaign().getSendURL();
    }

    @Transactional
    public SendMessageResponseDto getSendMessages(User user, Long campaignId) throws Exception{
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new GlobalException(ErrorCode.DATA_NOT_FOUND));
        if(campaign.getUser().getUserId() != user.getUserId()){
            throw new GlobalException(ErrorCode.DATA_NOT_FOUND);
        }
        try{
            updateSendMessages(campaign);
            // 조회 성공 표시
        } catch (Exception e){
            System.out.println("e = " + e.getMessage());
            e.printStackTrace();
        }
        List<SendMessages> allByCampaign = sendMessagesRepository.findAllByCampaign(campaign);
        List<SendMessageElementDto> list = allByCampaign
                .stream()
                .map((e)-> new SendMessageElementDto(e.getSendMessagesId(),e.getSendDateTime(),e.getName(), e.getPhoneNumber(),e.getSendState(),e.getErrorMessage()))
                .toList();
        return new SendMessageResponseDto(allByCampaign.size(), list);
    }
    @Transactional
    public Long createCampaign(User user, CampaignRequestDto requestDto) throws Exception{
        return createCampaign(user,requestDto, true);
    }

    @Transactional
    public Long createCampaign(User user, CampaignRequestDto requestDto , Boolean sendMessage) throws Exception{

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime parse = LocalDateTime.parse(requestDto.getSendingDate());
        requestDto.setSendingDate(parse.format(dateTimeFormatter));

        if(requestDto.getSendType().equals("ad")){
            requestDto.setMessageA("(광고)" + requestDto.getMessageA());
            if(!(requestDto.getMessageB()==null||requestDto.getMessageB().equals(""))){
                requestDto.setMessageB("(광고)" + requestDto.getMessageB());
            }
        }

        CustomerGroup customerGroup = customerGroupRepository
                .findById(requestDto.getCustomerGroupId()).orElseThrow(() -> new GlobalException(ErrorCode.DATA_NOT_FOUND));
        customerGroup.makeUnableEdit();

        CopyGroup copyGroup = copyGroupRepository
                .findById(requestDto.getCopyGroupId()).orElseThrow(() -> new GlobalException(ErrorCode.DATA_NOT_FOUND));
        copyGroup.makeUnableEdit();

        LocalDateTime sendingDate = LocalDateTime.now();
        if(requestDto.getSendingDate() != null){
            sendingDate = LocalDateTime.parse(requestDto.getSendingDate(), dateTimeFormatter);
        }

        Campaign campaign = Campaign.builder()
                .campaignName(requestDto.getCampaignName())
                .createdAt(LocalDateTime.now())
                .user(user)
                .customerGroup(customerGroup)
                .copyGroup(copyGroup)
                .favorite(false)
                .messageType(requestDto.getMessageType())
                .sendURL(requestDto.getSendURL())
                .sendingDate(sendingDate)
                .build();
        campaign = campaignRepository.save(campaign);

        CampaignMessage campaignMessageA = CampaignMessage.builder()
                .message(requestDto.getMessageA())
                .campaign(campaign)
                .messageSection("A")
                .build();
        campaignMessageA = campaignMessageRepository.save(campaignMessageA);
        CampaignMessage campaignMessageB;


        // AB 테스트 사용 X
        if(requestDto.getMessageB()==null||requestDto.getMessageB().equals("")){
            log.info("AB 테스트 사용 X");
            ArrayList<NaverApiMessage> apiMessagesList = new ArrayList<>();
            String suffix = " [무료 수신거부] " + messageSource.getMessage("key.denial", null, null);
            ExcelFile excelFile = customerGroup.getExcelFile();
            List<ExcelData> excelFileList = excelDataRepository.findAllByExcelFile(excelFile);

            for(int i=0;i<excelFileList.size(); i++) {
                ExcelData excelData = excelFileList.get(i);
                String url = null;
                while (true) {
                    url = makeUrl();
                    if (sendMessagesRepository.findByUniqueUrl(url).isEmpty()) {
                        break;
                    }
                }
                SendMessages sendMessages = SendMessages.builder()
                        .campaign(campaign)
                        .campaignMessage(campaignMessageA)
                        .sendDateTime(sendingDate)
                        .name(excelData.getUsername())
                        .phoneNumber(excelData.getPhoneNumber())
                        .sendCheck(false)
                        .errorMessage(null)
                        .uniqueUrl(url)
                        .visitedTime(null)
                        .visitedDate(null)
                        .sendState("발송대기")
                        .build();
                sendMessages = sendMessagesRepository.save(sendMessages);
                apiMessagesList.add(new NaverApiMessage(excelData.getPhoneNumber().replace("-", ""), "(광고)메세지",
                        campaignMessageA.getMessage() + " " + messageSource.getMessage("url", null, null) + sendMessages.getUniqueUrl() + suffix));
            }

            if(sendMessage){
                //send massage
                String requestId = naverApiLmsSend(apiMessagesList, requestDto.getSendingDate(), requestDto.getSendType());
                campaignMessageA.addSendRequestId(requestId);
                campaign.addApiKey(requestId);
            }

        }else{ // AB 테스트 사용 O
            log.info("AB 테스트 사용 O");
            log.info("메세지 A : {}", requestDto.getMessageA());
            log.info("메세지 B : {}", requestDto.getMessageB());

            campaignMessageB = CampaignMessage.builder()
                    .message(requestDto.getMessageB())
                    .campaign(campaign)
                    .messageSection("B")
                    .build();
            campaignMessageB = campaignMessageRepository.save(campaignMessageB);


            ExcelFile excelFile = customerGroup.getExcelFile();
            ArrayList<NaverApiMessage> naverApiMessages =
                    splitMessageList(excelDataRepository.findAllByExcelFile(excelFile), campaignMessageA, campaignMessageB, campaign, requestDto);

            if(sendMessage) {
                //send massage
                String requestId = naverApiLmsSend(naverApiMessages, requestDto.getSendingDate(), requestDto.getSendType());
                campaign.addApiKey(requestId);
                campaignMessageA.addSendRequestId(requestId);
                campaignMessageB.addSendRequestId(requestId);
            }
        }
        return campaign.getCampaignId();
    }

    private ArrayList<NaverApiMessage> splitMessageList(
            List<ExcelData> excelDataList, CampaignMessage messageA, CampaignMessage messageB, Campaign campaign, CampaignRequestDto requestDto){
        ArrayList<NaverApiMessage> apiMessagesList = new ArrayList<>();
        String suffix = " [무료 수신거부] " + messageSource.getMessage("key.denial", null, null);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime sendingDate = LocalDateTime.now();
        if(requestDto.getSendingDate() != null){
            sendingDate = LocalDateTime.parse(requestDto.getSendingDate(), dateTimeFormatter);
        }

        for(int i=0; i < excelDataList.size() / 2; i++ ){
            ExcelData excelData = excelDataList.get(i);
            String url = null;
            while(true){
                url = makeUrl();
                if(sendMessagesRepository.findByUniqueUrl(url).isEmpty()){
                    break;
                }
            }
            SendMessages sendMessages = SendMessages.builder()
                    .campaign(campaign)
                    .campaignMessage(messageA)
                    .sendDateTime(sendingDate)
                    .name(excelData.getUsername())
                    .phoneNumber(excelData.getPhoneNumber())
                    .sendCheck(false)
                    .errorMessage(null)
                    .uniqueUrl(url)
                    .visitedTime(null)
                    .visitedDate(null)
                    .sendState("발송대기")
                    .build();
            sendMessages = sendMessagesRepository.save(sendMessages);
            apiMessagesList.add(new NaverApiMessage( excelData.getPhoneNumber().replace("-", ""), "(광고)메세지" ,
                    messageA.getMessage() + " " + messageSource.getMessage("url",null,null) + sendMessages.getUniqueUrl() + suffix));
        }
        for (int i = excelDataList.size() / 2; i < excelDataList.size(); i++) {
            ExcelData excelData = excelDataList.get(i);
            String url = null;
            while(true){
                url = makeUrl();
                if(sendMessagesRepository.findByUniqueUrl(url).isEmpty()){
                    break;
                }
            }
            SendMessages sendMessages = SendMessages.builder()
                    .campaign(campaign)
                    .campaignMessage(messageB)
                    .sendDateTime(sendingDate)
                    .name(excelData.getUsername())
                    .phoneNumber(excelData.getPhoneNumber())
                    .sendCheck(false)
                    .errorMessage(null)
                    .uniqueUrl(url)
                    .visitedTime(null)
                    .visitedDate(null)
                    .sendState("발송대기")
                    .build();
            sendMessages = sendMessagesRepository.save(sendMessages);
            apiMessagesList.add(new NaverApiMessage( excelData.getPhoneNumber().replace("-", ""), "(광고)메세지" ,
                    messageB.getMessage() + " " + messageSource.getMessage("url",null,null) + sendMessages.getUniqueUrl() + suffix));
        }

        return apiMessagesList;
    }

    private String naverApiLmsSend(ArrayList<NaverApiMessage> messages,String reserveTime,String contentType) throws Exception {

        String timestamp = Long.toString(System.currentTimeMillis());
        String url = "https://sens.apigw.ntruss.com/sms/v2/services/" + messageSource.getMessage("key.naverserviceId", null, null) + "/messages";
        String uri = "/sms/v2/services/" + messageSource.getMessage("key.naverserviceId", null, null) + "/messages";
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/json; charset=utf-8");
        headers.add("x-ncp-apigw-timestamp", timestamp);
        headers.add("x-ncp-iam-access-key", messageSource.getMessage("key.naverAccess",null,null));
        headers.add("x-ncp-apigw-signature-v2", makeSignature(timestamp, uri, "POST"));

        // Body 생성
        NaverApiRequestDto dto = NaverApiRequestDto.builder()
                .type("LMS")
                .contentType(contentType)
                .countryCode("82")
                .from(messageSource.getMessage("key.naverfrom", null, null))
                .subject("기본 문자 제목")
                .content("기본 문자 발송 내용")
                .messages(messages)
                .reserveTime(reserveTime)  //.reserveTime("2023-03-07 21:23")
                .build();

        // HTTP 요청 보내기
        HttpEntity<String> request =new HttpEntity<>(mapper.writeValueAsString(dto),headers);
        System.out.println("Request = " + request);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );
        log.info("Request Body = {}", request);

        // HTTP 응답 (JSON)
        String responseBody = response.getBody();
        log.info("responseBody = {}", responseBody);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("requestId").asText();

//        String returnVal  = jsonNode.get("statusName").asText();
//        System.out.println("returnVal = " + returnVal);

//        String returnVal  = jsonNode.get("message").get("result").get("translatedText").asText();
//        System.out.println("jsonNode = " + jsonNode);
//        System.out.println("answer = " + returnVal);
    }

    public String makeSignature(String timestamp, String url, String method) throws Exception{
        String space = " ";					// one space
        String newLine = "\n";					// new line
//        String method = "POST";					// method
//        String url = ;	// url (include query string)
//        String timestamp = Instant.now();			// current timestamp (epoch)
        String accessKey = messageSource.getMessage("key.naverAccess",null,null);			// access key id (from portal or Sub Account)
        String secretKey = messageSource.getMessage("key.naverSecret",null,null);

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    public String makeUrl() {
        Random random = new Random();
        StringBuilder url = new StringBuilder();
        for(int i=0;i<5; i++){
            int n = random.nextInt(36);
            if(n<26){
                if(random.nextBoolean()){
                    url.append((char)(n +'a'));
                }else {
                    url.append((char)(n+'A'));
                }
            }else {
                url.append((char)((n-26)+'0'));
            }
        }
        return url.toString();
    }

    @Transactional
    public void updateSendMessages(Campaign campaign) throws Exception{
        String timestamp = Long.toString(System.currentTimeMillis());
        String requestId = campaign.getApiKey();

        String url = "https://sens.apigw.ntruss.com/sms/v2/services/" + messageSource.getMessage("key.naverserviceId", null, null) + "/messages?requestId=" + requestId;
        String uri = "/sms/v2/services/" + messageSource.getMessage("key.naverserviceId", null, null) + "/messages?requestId=" + requestId;

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/json; charset=utf-8");
        headers.add("x-ncp-apigw-timestamp", timestamp);
        headers.add("x-ncp-iam-access-key", messageSource.getMessage("key.naverAccess",null,null));
        headers.add("x-ncp-apigw-signature-v2", makeSignature(timestamp, uri, "GET"));

        // HTTP 요청 보내기
        HttpEntity<String> Request =new HttpEntity<>("",headers);
        System.out.println("Request = " + Request);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                url,
                HttpMethod.GET,
                Request,
                String.class
        );

        // HTTP 응답 (JSON)
        String responseBody = response.getBody();
//        System.out.println("responseBody = " + responseBody);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        //SendMessages 조회
        List<SendMessages> sendMessagesList = sendMessagesRepository.findAllByCampaign(campaign);

        //데이터 파싱
        log.info("responseBody : {}", jsonNode);
        int itemCount = jsonNode.get("itemCount").asInt();
        log.info("updateSendMessages : execute [{}] update", itemCount);

        for(int i=0;i<itemCount;i++){
            NaverApiMessageResultDto messages = objectMapper.readValue(jsonNode.get("messages").get(i).toString(), NaverApiMessageResultDto.class);
            log.info("input NaverApiMessageResultDto : {}", messages);
            for (SendMessages sendMessages : sendMessagesList) {
                String status;
                if(messages.getStatusCode()!=null && messages.getStatusCode().equals("0")){
                    status = "발송성공";
                }else if(messages.getStatus().equals("READY")) {
                    status = "발송대기";
                }else if(messages.getStatus().equals("PROCESSING")) {
                    status = "발송중";
                }else{
                    status = "발송실패";
                }

                if(sendMessages.getPhoneNumber().replace("-", "").equals(messages.getTo())){
                    log.info("updateData to [{}] data : sendState = {} , errorMessage = {}",sendMessages.getPhoneNumber(),status, messages.getStatusMessage());
                    sendMessages.updateData(status ,messages.getStatusMessage());
                }
            }
        }
    }
}
