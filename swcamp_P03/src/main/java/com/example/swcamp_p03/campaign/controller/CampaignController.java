package com.example.swcamp_p03.campaign.controller;


import com.example.swcamp_p03.campaign.dto.request.CampaignRequestDto;
import com.example.swcamp_p03.campaign.dto.request.CommentRequestDto;
import com.example.swcamp_p03.campaign.dto.response.CampaignCreateResponseDto;
import com.example.swcamp_p03.campaign.dto.response.CampaignDetailDto;
import com.example.swcamp_p03.campaign.dto.response.SendMessageResponseDto;
import com.example.swcamp_p03.campaign.dto.response.TotalCampaignResponseDto;
import com.example.swcamp_p03.campaign.service.CampaignReadService;
import com.example.swcamp_p03.campaign.service.CampaignService;
import com.example.swcamp_p03.campaign.service.CampaignWriteService;
import com.example.swcamp_p03.common.dto.ResponseDto;
import com.example.swcamp_p03.common.dto.SearchDto;
import com.example.swcamp_p03.config.UserDetailsImpl;
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

    private final CampaignService campaignService;
    private final CampaignReadService campaignReadService;
    private final CampaignWriteService campaignWriteService;
    
    @PostMapping("/campaign")
    public CampaignCreateResponseDto createCampaign(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CampaignRequestDto requestDto) throws Exception {
        Long campaignId = campaignService.createCampaign(userDetails.getUser(), requestDto);
        return new CampaignCreateResponseDto(campaignId, "success");
    }

    @GetMapping("/campaigns/{campaignId}/sendmessages")
    public ResponseDto<SendMessageResponseDto> getSendMessages(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long campaignId) throws Exception {
        SendMessageResponseDto sendMessages = campaignService.getSendMessages(userDetails.getUser(), campaignId);
        return ResponseDto.success(sendMessages);
    }


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
        campaignWriteService.writeComment(campaignId, requestDto);
    }

    @GetMapping("/campaigns/{campaignId}")
    public ResponseDto<CampaignDetailDto> getDetailCampaign(@PathVariable Long campaignId) throws Exception {
        return campaignReadService.getDetailCampaign(campaignId);
    }

}
