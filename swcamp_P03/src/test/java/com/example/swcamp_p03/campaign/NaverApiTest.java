package com.example.swcamp_p03.campaign;

import com.example.swcamp_p03.campaign.dto.api.NaverApiMessage;
import com.example.swcamp_p03.campaign.dto.api.NaverApiMessageResultDto;
import com.example.swcamp_p03.campaign.dto.api.NaverApiRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class NaverApiTest {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ObjectMapper mapper;

//    @Test
    public void naverApiTest() throws Exception {

        String timestamp = Long.toString(System.currentTimeMillis());
        String url = "https://sens.apigw.ntruss.com/sms/v2/services/" + messageSource.getMessage("key.naverserviceId", null, null) + "/messages";
        String uri = "/sms/v2/services/" + messageSource.getMessage("key.naverserviceId", null, null) + "/messages";
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        System.out.println("api 사용 제한 때문에 혹시 모르고 호출할 경우를 대비해 주석처리 하였음");
//        headers.add("Content-type", "application/json; charset=utf-8");
//        headers.add("x-ncp-apigw-timestamp", timestamp);
//        headers.add("x-ncp-iam-access-key", messageSource.getMessage("key.naverAccess",null,null));
//        headers.add("x-ncp-apigw-signature-v2", makeSignature(timestamp, uri, "POST"));

        // Body 생성
        ArrayList<NaverApiMessage> messages = new ArrayList<>();
        messages.add(new NaverApiMessage("01033136371", "개별 메시지 제목","test 위의 content 와 별도로 해당 번호로만 보내는 내용(optional)"));
        messages.add(new NaverApiMessage("01073725466", "개별 메시지 제목","(광고) 찬진님 한테 보내는 광고메세지 [무료수신거부]: "+ messageSource.getMessage("key.denial",null,null)));

        NaverApiRequestDto dto = NaverApiRequestDto.builder()
                .type("SMS")
                .contentType("AD")
                .countryCode("82")
                .from(messageSource.getMessage("key.naverfrom", null, null))
                .subject("기본 메시지 제목")
                .content("테스트 문자 발송 내용")
                .messages(messages)
//                .reserveTime("2023-03-07 21:23")
                .build();

        // HTTP 요청 보내기
        HttpEntity<String> Request =new HttpEntity<>(mapper.writeValueAsString(dto),headers);
        System.out.println("Request = " + Request);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                url,
                HttpMethod.POST,
                Request,
                String.class
        );



//        System.out.println("bodyJson = " + body);

        // HTTP 응답 (JSON)
        String responseBody = response.getBody();
        System.out.println("responseBody = " + responseBody);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String returnVal  = jsonNode.get("statusName").asText();
        System.out.println("returnVal = " + returnVal);

//        String returnVal  = jsonNode.get("message").get("result").get("translatedText").asText();

//        System.out.println("jsonNode = " + jsonNode);
//        System.out.println("answer = " + returnVal);
//        responseBody = {"statusCode":"202","statusName":"success","requestId":"VORSSA-1678195573358-3988-57284298-VYpgGaRD","requestTime":"2023-03-07T22:26:13.358"}
    }

//    @Test
    public void getNaverApiRequestResultTest() throws Exception {
        String timestamp = Long.toString(System.currentTimeMillis());
        String requestId = "VORSSA-1678340434941-1843-57284298-SZzDXTJp";

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


        //데이터 파싱
        System.out.println("jsonNode = " + jsonNode);
        int itemCount = jsonNode.get("itemCount").asInt();
        System.out.println("itemCount = " + itemCount);

        for(int i=0;i<itemCount;i++){
            NaverApiMessageResultDto messages = objectMapper.readValue(jsonNode.get("messages").get(i).toString(), NaverApiMessageResultDto.class);
            System.out.println("messages = " + messages);
        }
    }

    @Test
    public void jsonBuildTest() throws Exception{
        // Body 생성
        ArrayList<NaverApiMessage> messages = new ArrayList<>();
        messages.add(new NaverApiMessage("01033136371", "개별 메시지 제목","test 위의 content 와 별도로 해당 번호로만 보내는 내용(optional)"));
        messages.add(new NaverApiMessage("01073725466", "개별 메시지 제목","test 위의 content 와 별도로 해당 번호로만 보내는 내용(optional)"));

        NaverApiRequestDto dto = NaverApiRequestDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(messageSource.getMessage("key.naverfrom", null, null))
                .subject("기본 메시지 제목")
                .content("테스트 문자 발송 내용")
                .messages(messages)
                .reserveTime("2023-03-07 21:15")
                .build();

        System.out.println("dto = " + mapper.writeValueAsString(dto));

        long timestamp = System.currentTimeMillis();
        String signature = makeSignature(Long.toString(timestamp), "/sms/v2/services/" + messageSource.getMessage("key.naverserviceId", null, null) + "/messages", "GET");
        System.out.println("signature = " + signature);
        System.out.println("timestamp = " + timestamp);
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
}
