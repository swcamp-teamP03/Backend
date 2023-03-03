package com.example.swcamp_p03.copyGroup.service;

import com.example.swcamp_p03.common.exception.ErrorCode;
import com.example.swcamp_p03.common.exception.GlobalException;
import com.example.swcamp_p03.copyGroup.dto.*;
import com.example.swcamp_p03.copyGroup.entity.CopyGroup;
import com.example.swcamp_p03.copyGroup.entity.GptCopy;
import com.example.swcamp_p03.copyGroup.repository.CopyGroupRepository;
import com.example.swcamp_p03.copyGroup.repository.GptCopyRepository;
import com.example.swcamp_p03.user.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CopyGroupService {

    private final CopyGroupRepository copyGroupRepository;
    private final MessageSource messageSource;
    private final GptCopyRepository gptCopyRepository;


    public CopyGroupListResponseDto copyGroupList( User user, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        List<CopyGroupListElementDto> list = copyGroupRepository
                .findAllByUser(user,pageable)
                    .stream()
                    .map(e -> new CopyGroupListElementDto(e))
                    .toList();
        return new CopyGroupListResponseDto(list.size(),list);
    }

    public CopyGroupDto getCopyGroup( User user, Long copyGroupId){
        CopyGroup copyGroup = copyGroupRepository.findById(copyGroupId).orElseThrow(() -> new GlobalException(ErrorCode.DATA_NOT_FOUND));
        if(user.getUserId() != copyGroup.getUser().getUserId()){
            throw new GlobalException(ErrorCode.DATA_NOT_FOUND);
        }
        CopyGroupDto responseDto = new CopyGroupDto(copyGroup);
        for (GptCopy gptCopy : copyGroup.getGptCopyList()) {
            responseDto.getCopyList().add(new GptCopyDto(gptCopy));
        }
        return responseDto;
    }

    @Transactional
    public Long createCopyGroup(User user, CopyGroupDto createDto) {
        if (Integer.parseInt(createDto.getCreateCount()) != createDto.getCopyList().size()) {
            throw new GlobalException(ErrorCode.VALIDATION_FAIL);
        }

        CopyGroup copyGroup = CopyGroup.builder()
                .coupGroupName(createDto.getCopyGroupName())
                .tag(createDto.getTag())
                .brandName(createDto.getBrandName())
                .productName(createDto.getProductName())
                .keyword(createDto.getKeyword())
                .copyType(createDto.getType())
                .favorite(false)
                .createCount(Integer.parseInt(createDto.getCreateCount()))
                .copyLength(Integer.parseInt(createDto.getCopyLength()))
                .user(user)
                .gptCopyList(new ArrayList<>())
                .build();

        List<GptCopyDto> copyList = createDto.getCopyList();
        for (GptCopyDto gptCopyDto : copyList) {
            copyGroup.addGptCopy(new GptCopy(gptCopyDto.getContent(), gptCopyDto.getState(), copyGroup, gptCopyDto.getPin()));
        }

        return copyGroupRepository.save(copyGroup).getCopyGroupId();
    }

    public String getGptCopy(String question) throws Exception{
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/json");
        headers.add("Authorization", "Bearer " + messageSource.getMessage("key.openAI", null, null));

        // Body 생성
        String bodyJson = "{\r\n" +
                "  \"model\": \"text-davinci-003\",\r\n" +
                "    \"prompt\": \"" + question + "\",\r\n" +
                "    \"max_tokens\": 2000,\r\n" +
                "    \"user\": \"1\"\r\n"+
                "}";

        log.trace("bodyJson = {}", bodyJson);

        // HTTP 요청 보내기
        HttpEntity<String> kakaoTokenRequest =new HttpEntity<>(bodyJson,headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://api.openai.com/v1/completions",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON)
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String answer  = jsonNode.get("choices").get(0).get("text").asText();


        log.trace("jsonNode = {}", jsonNode);
        log.trace("answer = {}", answer);

        return answer;
    }

    @Transactional
    public CopyGroupLikeDto CopyGroupLike(User user , Long copyGroupId, CopyGroupLikeDto requestDto) {
        CopyGroup copyGroup = copyGroupRepository.findById(copyGroupId).orElseThrow(() -> new GlobalException(ErrorCode.DATA_NOT_FOUND));
        if(user.getUserId()!=copyGroup.getUser().getUserId()){
            throw new GlobalException(ErrorCode.DATA_NOT_FOUND);
        }

        copyGroup.clickFavorite(requestDto.getFavorite());
        return requestDto;
    }

    @Transactional
    public CopyReportResponseDto CopyReport(User user , Long copyId){

        GptCopy gptCopy = gptCopyRepository.findById(copyId).orElseThrow(() -> new GlobalException(ErrorCode.DATA_NOT_FOUND));
        if(gptCopy.getCopyGroup().getUser().getUserId() != user.getUserId()){
            throw new GlobalException(ErrorCode.DATA_NOT_FOUND);
        }
        gptCopy.report();
        return new CopyReportResponseDto("success");
    }

}
