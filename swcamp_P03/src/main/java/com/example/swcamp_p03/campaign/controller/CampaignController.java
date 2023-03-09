package com.example.swcamp_p03.campaign.controller;

import com.example.swcamp_p03.campaign.dto.request.CampaignRequestDto;
import com.example.swcamp_p03.campaign.dto.response.CampaignCreateResponseDto;
import com.example.swcamp_p03.campaign.dto.response.SendMessageResponseDto;
import com.example.swcamp_p03.campaign.service.CampaignService;
import com.example.swcamp_p03.common.dto.ResponseDto;
import com.example.swcamp_p03.config.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CampaignController {

    private final CampaignService campaignService;
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

}
