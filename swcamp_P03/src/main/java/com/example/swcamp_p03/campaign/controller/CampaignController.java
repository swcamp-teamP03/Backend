package com.example.swcamp_p03.campaign.controller;

import com.example.swcamp_p03.campaign.dto.request.CommentRequestDto;
import com.example.swcamp_p03.campaign.dto.response.TotalCampaignResponseDto;
import com.example.swcamp_p03.campaign.service.CampaignReadService;
import com.example.swcamp_p03.campaign.service.CampaignWriteService;
import com.example.swcamp_p03.common.dto.ResponseDto;
import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.customerGroup.dto.SearchDto;
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
public class CampaignController {
    private final CampaignReadService campaignReadService;
    private final CampaignWriteService campaignWriteService;

    @GetMapping("/campaigns")
    public ResponseDto<TotalCampaignResponseDto> getTotalCampaign(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                                                  Pageable pageable) {
        return campaignReadService.getTotalCampaign(userDetails.getUser(), pageable);
    }

    @GetMapping("/campaigns/search")
    public ResponseDto<TotalCampaignResponseDto> getSearch(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestParam("keyword") String keyword,
                                                           @RequestParam(value = "startDate", required = false, defaultValue = "1900-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                           @RequestParam(value = "endDate", required = false, defaultValue = "2300-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                           Pageable pageable) {
        SearchDto searchDto = SearchDto.builder()
                .user(userDetails.getUser())
                .search(keyword)
                .startDate(startDate)
                .endDate(endDate)
                .pageable(pageable)
                .build();
        return campaignReadService.getSearch(searchDto);
    }

    @PostMapping("/campaigns/{campaignId}")
    public void favorite(@PathVariable Long campaignId) {
        campaignWriteService.favoriteCheck(campaignId);
    }

    @PostMapping("/campaigns/{campaignId}/comment")
    public void writeComment(@PathVariable Long campaignId, @RequestBody CommentRequestDto requestDto) {
        campaignWriteService.writeComment(campaignId,requestDto);
    }
}
