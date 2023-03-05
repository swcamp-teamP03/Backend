package com.example.swcamp_p03.copyGroup.controller;

import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.copyGroup.dto.*;
import com.example.swcamp_p03.copyGroup.dto.request.CreateCopyRequestDto;
import com.example.swcamp_p03.copyGroup.dto.request.CreateCopyResponseDto;
import com.example.swcamp_p03.copyGroup.dto.response.CopyGroupCreateResponseDto;
import com.example.swcamp_p03.copyGroup.dto.response.CopyGroupListResponseDto;
import com.example.swcamp_p03.copyGroup.dto.response.CopyReportResponseDto;
import com.example.swcamp_p03.copyGroup.service.CopyGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public CopyGroupCreateResponseDto createCopyGroup(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CopyGroupDto copyGroupDto){
        Long copyGroupId = copyGroupService.createCopyGroup(userDetails.getUser(), copyGroupDto);
        return new CopyGroupCreateResponseDto(copyGroupId.toString(), "success");
    }

    @PutMapping("/copy/{copyGroupId}")
    public CopyGroupDto updateCopyGroup(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long copyGroupId,
            @RequestBody CopyGroupDto copyGroupDto) {
        return copyGroupService.updateCopyGroup(userDetails.getUser(),copyGroupDto , copyGroupId);
    }

    @GetMapping("/copy/{copyGroupId}")
    public CopyGroupDto getCopyGroup(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long copyGroupId){
        return copyGroupService.getCopyGroup(userDetails.getUser(), copyGroupId);
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

    @PostMapping("/copy/{copyGroupId}/like")
    public CopyGroupLikeDto CopyGroupLike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long copyGroupId, @RequestBody CopyGroupLikeDto requestDto){
        copyGroupService.CopyGroupLike(userDetails.getUser(), copyGroupId, requestDto);
        return requestDto;
    }

    @PostMapping("/copy/{copyId}/report")
    public CopyReportResponseDto CopyReport(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long copyId) {
        return copyGroupService.CopyReport(userDetails.getUser(),copyId);
    }

}
