package com.example.swcamp_p03.campaign.service;

import org.junit.jupiter.api.Test;

class CampaignServiceMakeUrlTest {

    CampaignService campaignService = new CampaignService(null,null,null,null,null,null,null,null);
    @Test
    void makeUrlTest(){
        for(int i=0;i<100;i++){
            String s = campaignService.makeUrl();
            System.out.println("makeUrl = " + s);
        }
    }

}
