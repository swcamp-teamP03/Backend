package com.example.swcamp_p03.campaign.service;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

class CampaignServiceMakeUrlTest {

    private CampaignService campaignService = new CampaignService(null,null,null,null,null,null,null,null);
    @Test
    void makeUrlTest(){
        String pattern = "^[a-zA-Z0-9]*$";
        for(int i=0;i<100;i++){
            String s = campaignService.makeUrl();
            boolean result = Pattern.matches(pattern, s);
            System.out.println("makeUrl = " + s + " [validate : " + result + "]");
        }
    }

}
