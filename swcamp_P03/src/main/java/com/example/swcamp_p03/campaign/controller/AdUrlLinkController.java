package com.example.swcamp_p03.campaign.controller;

import com.example.swcamp_p03.campaign.service.CampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdUrlLinkController {

    private final CampaignService campaignService;

    @GetMapping("/redirect/{uniqueUrl}")
    public String adUrlLink(@PathVariable String uniqueUrl){
        String url = campaignService.visitUrl(uniqueUrl);
        if(url.contains("http://")||url.contains("https://")){
            return "redirect:" + url;
        }
        return "redirect:http://" + url;
    }
}
