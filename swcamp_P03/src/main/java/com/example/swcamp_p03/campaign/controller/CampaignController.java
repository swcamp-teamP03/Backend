package com.example.swcamp_p03.campaign.controller;

import com.example.swcamp_p03.campaign.dto.request.CampaignRequestDto;
import com.example.swcamp_p03.campaign.dto.response.CampaignCreateResponseDto;
import com.example.swcamp_p03.campaign.service.CampaignService;
import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CampaignController {

    private final CampaignService campaignService;
    @PostMapping("/campaign")
    public CampaignCreateResponseDto createCampaign(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CampaignRequestDto requestDto) throws Exception{
        Long campaignId = campaignService.createCampaign(userDetails.getUser(), requestDto);
        return new CampaignCreateResponseDto(campaignId, "success");
    }

}
