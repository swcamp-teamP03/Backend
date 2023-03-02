package com.example.swcamp_p03.copyGroup.controller;

import com.example.swcamp_p03.common.dto.ResponseDto;
import com.example.swcamp_p03.common.exception.GlobalException;
import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.copyGroup.dto.*;
import com.example.swcamp_p03.copyGroup.service.CopyGroupService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CopyGroupController {

    private final CopyGroupService copyGroupService;

    @GetMapping("/copies")
    public CopyGroupListResponseDto getCopyGroupList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "4") int size){
        return copyGroupService.copyGroupList( userDetails.getUser(), page, size);
    }

    @PostMapping("/copy")
    public CopyGroupCreateResponseDto createCopyGroup(@AuthenticationPrincipal UserDetailsImpl userDetails,@RequestBody CopyGroupCreateDto copyGroupCreateDto){
        Long copyGroupId = copyGroupService.createCopyGroup(userDetails.getUser(), copyGroupCreateDto);
        return new CopyGroupCreateResponseDto(copyGroupId.toString(), "success");
    }

    @PostMapping("/gptcopy")
    public CreateCopyResponseDto createCopy(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CreateCopyRequestDto requestDto) throws Exception{
        String question = "브랜드이름 " + requestDto.getBrandName() + "은 " + requestDto.getSector() + "업종이야."
                + requestDto.getBrandName() + "의 제품인 " + requestDto.getProductName() + "에 대해 "
                + requestDto.getKeyword() + "를 포함해서 " + requestDto.getType() + " 문자메시지 형식으로 "
                + requestDto.getCopyLength() + "글자에 맞춰 써줘.";

        CreateCopyResponseDto responseDto = new CreateCopyResponseDto();
        for(int i = 0; i<Integer.parseInt(requestDto.getCreateCount()); i++){
            responseDto.addCopy(copyGroupService.getGptCopy(question));
        }

        return responseDto;
    }

}
