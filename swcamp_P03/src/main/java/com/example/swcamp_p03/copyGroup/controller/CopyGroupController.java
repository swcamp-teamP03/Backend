package com.example.swcamp_p03.copyGroup.controller;

import com.example.swcamp_p03.common.dto.ResponseDto;
import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.copyGroup.dto.*;
import com.example.swcamp_p03.copyGroup.dto.request.CreateCopyRequestDto;
import com.example.swcamp_p03.copyGroup.dto.response.CreateCopyResponseDto;
import com.example.swcamp_p03.copyGroup.dto.response.CopyGroupCreateResponseDto;
import com.example.swcamp_p03.copyGroup.dto.response.CopyGroupListResponseDto;
import com.example.swcamp_p03.copyGroup.dto.response.CopyReportResponseDto;
import com.example.swcamp_p03.copyGroup.service.CopyGroupService;
import com.example.swcamp_p03.customerGroup.dto.SearchDto;
import com.example.swcamp_p03.customerGroup.dto.reponse.TotalGroupResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CopyGroupController {

    private final CopyGroupService copyGroupService;

    @GetMapping("/copies")
    public ResponseDto<CopyGroupListResponseDto> getCopyGroupList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                                                  Pageable pageable) {
        return ResponseDto.success(copyGroupService.copyGroupList(userDetails.getUser(), pageable));
    }

    @GetMapping("/copies/search")
    public ResponseDto<CopyGroupListResponseDto> getSearch(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestParam("keyword") String keyword,
                                                        @RequestParam(value = "startDate",required = false,  defaultValue = "1900-01-01")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                        @RequestParam(value = "endDate",required = false,  defaultValue = "2300-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                        Pageable pageable
    ) {
        SearchDto searchDto = SearchDto.builder()
                .user(userDetails.getUser())
                .search(keyword)
                .startDate(startDate)
                .endDate(endDate)
                .pageable(pageable)
                .build();
        return ResponseDto.success(copyGroupService.getSearch(searchDto));
    }

    @PostMapping("/copy")
    public ResponseDto<CopyGroupCreateResponseDto> createCopyGroup(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CopyGroupDto copyGroupDto) {
        Long copyGroupId = copyGroupService.createCopyGroup(userDetails.getUser(), copyGroupDto);
        return ResponseDto.success(new CopyGroupCreateResponseDto(copyGroupId, "success"));
    }

    @PutMapping("/copy/{copyGroupId}")
    public ResponseDto<CopyGroupDto> updateCopyGroup(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long copyGroupId,
            @RequestBody CopyGroupDto copyGroupDto) {
        return ResponseDto.success(copyGroupService.updateCopyGroup(userDetails.getUser(), copyGroupDto, copyGroupId));
    }

    @GetMapping("/copy/{copyGroupId}")
    public ResponseDto<CopyGroupDto> getCopyGroup(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long copyGroupId) {
        return ResponseDto.success(copyGroupService.getCopyGroup(userDetails.getUser(), copyGroupId));
    }

    @PostMapping("/gptcopy")
    public ResponseDto<CreateCopyResponseDto> createCopy(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CreateCopyRequestDto requestDto) throws Exception {
        String question = "브랜드이름 " + requestDto.getBrandName() + "은 " + requestDto.getSector() + "업종이야."
                + requestDto.getBrandName() + "의 제품인 " + requestDto.getProductName() + "에 대해 "
                + requestDto.getKeyword() + "를 포함해서 " + requestDto.getType() + " 문자메시지 형식으로 "
                + requestDto.getCopyLength() + "글자에 맞춰 써줘.";

        CreateCopyResponseDto responseDto = new CreateCopyResponseDto();
        for (int i = 0; i < requestDto.getCreateCount(); i++) {
            responseDto.addCopy(copyGroupService.getGptCopy(question));
        }

        return ResponseDto.success(responseDto);
    }

    @PostMapping("/copy/{copyGroupId}/like")
    public ResponseDto<CopyGroupLikeDto> CopyGroupLike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long copyGroupId, @RequestBody CopyGroupLikeDto requestDto) {
        copyGroupService.CopyGroupLike(userDetails.getUser(), copyGroupId, requestDto);
        return ResponseDto.success(requestDto);
    }
}
