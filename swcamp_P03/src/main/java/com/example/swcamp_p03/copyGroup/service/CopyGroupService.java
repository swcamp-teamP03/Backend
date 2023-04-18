package com.example.swcamp_p03.copyGroup.service;

import com.example.swcamp_p03.common.exception.ErrorCode;
import com.example.swcamp_p03.common.exception.GlobalException;
import com.example.swcamp_p03.copyGroup.dto.*;
import com.example.swcamp_p03.copyGroup.dto.response.CopyGroupListResponseDto;
import com.example.swcamp_p03.copyGroup.entity.CopyGroup;
import com.example.swcamp_p03.copyGroup.entity.GptCopy;
import com.example.swcamp_p03.copyGroup.entity.history.CopyGroupHistory;
import com.example.swcamp_p03.copyGroup.repository.CopyGroupRepository;
import com.example.swcamp_p03.copyGroup.repository.GptCopyRepository;
import com.example.swcamp_p03.copyGroup.repository.history.CopyGroupHistoryRepository;
import com.example.swcamp_p03.common.dto.SearchDto;
import com.example.swcamp_p03.user.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
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
    private final CopyGroupHistoryRepository copyGroupHistoryRepository;
    private final EntityManager em;

    public CopyGroupListResponseDto copyGroupList(User user, Pageable pageable) {
        return copyGroupRepository.findTotalCopyGroup(user, pageable);
    }

    public CopyGroupDto getCopyGroup(User user, Long copyGroupId) {
        CopyGroup copyGroup = copyGroupRepository.findById(copyGroupId).orElseThrow(() -> new GlobalException(ErrorCode.DATA_NOT_FOUND));
        if (user.getUserId() != copyGroup.getUser().getUserId()) {
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

        CopyGroup copyGroup = CopyGroup.builder()
                .coupGroupName(createDto.getCopyGroupName())
                .brandName(createDto.getBrandName())
                .productName(createDto.getProductName())
                .keyword(createDto.getKeyword())
                .copyType(createDto.getType())
                .favorite(false)
                .sector(createDto.getSector())
                .createCount(createDto.getCreateCount())
                .targetAge(createDto.getTargetAge())
                .targetGender(createDto.getTargetGender())
                .user(user)
                .unableEdit(false)
                .gptCopyList(new ArrayList<>())
                .build();

        List<GptCopyDto> copyList = createDto.getCopyList();
        for (GptCopyDto gptCopyDto : copyList) {
            copyGroup.addGptCopy(new GptCopy(gptCopyDto.getContent(), copyGroup, gptCopyDto.getIsPinned(), LocalDateTime.now(), null));
        }

        return copyGroupRepository.save(copyGroup).getCopyGroupId();
    }

    @Transactional
    public CopyGroupDto updateCopyGroup(User user, CopyGroupDto copyGroupDto, Long copyGroupId) {

        CopyGroup copyGroup = copyGroupRepository.findById(copyGroupId).orElseThrow(() -> new GlobalException(ErrorCode.DATA_NOT_FOUND));

        if (copyGroup.getUnableEdit() == true) {
            throw new GlobalException(ErrorCode.CANT_EDIT);
        }

        copyGroupHistoryRepository.save(new CopyGroupHistory(copyGroup));

        List<GptCopy> gptCopyList = copyGroup.getGptCopyList();
        GptCopy gptCopyDel = gptCopyList.get(0);
        for (GptCopy gptCopy : gptCopyList) {
            gptCopyRepository.delete(gptCopy);
        }
        copyGroup.updateCopyList(
                copyGroupDto
                        .getCopyList()
                        .stream()
                        .map(c -> new GptCopy(c.getContent(), copyGroup, c.getIsPinned(), gptCopyDel.getCreatedAt(), LocalDateTime.now()))
                        .toList());

        em.flush();

        CopyGroupDto responseDto = new CopyGroupDto(copyGroup);
        for (GptCopy gptCopy : copyGroup.getGptCopyList()) {
            responseDto.getCopyList().add(new GptCopyDto(gptCopy));
        }
        return responseDto;
    }

    public String getGptCopy(String question) throws Exception {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/json");
        headers.add("Authorization", "Bearer " + messageSource.getMessage("key.openAI", null, null));

        // Body 생성
        String bodyJson = "{\r\n" +
                "  \"model\": \"text-davinci-003\",\r\n" +
                "    \"prompt\": \"" + question + "\",\r\n" +
                "    \"max_tokens\": 2000,\r\n" +
                "    \"user\": \"1\"\r\n" +
                "}";

        log.trace("bodyJson = {}", bodyJson);

        // HTTP 요청 보내기
        HttpEntity<String> kakaoTokenRequest = new HttpEntity<>(bodyJson, headers);
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
        String answer = jsonNode.get("choices").get(0).get("text").asText();


        log.trace("jsonNode = {}", jsonNode);
        log.trace("answer = {}", answer);

        return answer;
    }

    @Transactional
    public CopyGroupLikeDto CopyGroupLike(User user, Long copyGroupId, CopyGroupLikeDto requestDto) {
        CopyGroup copyGroup = copyGroupRepository.findById(copyGroupId).orElseThrow(() -> new GlobalException(ErrorCode.DATA_NOT_FOUND));
        if (user.getUserId() != copyGroup.getUser().getUserId()) {
            throw new GlobalException(ErrorCode.DATA_NOT_FOUND);
        }

        copyGroup.clickFavorite(requestDto.getFavorite());
        return requestDto;
    }

    public CopyGroupListResponseDto getSearch(SearchDto searchDto) {
        return copyGroupRepository.findSearch(searchDto);
    }
}
