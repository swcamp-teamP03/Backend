package com.example.swcamp_p03.campaign.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class NaverApiRequestDto {
    private String type;
    private String contentType;
    private String countryCode;
    private String from;        //"01012345678"
    private String subject;
    private String content;
    private ArrayList<NaverApiMessage> messages;
    private String reserveTime;     // "yyyy-MM-dd HH:mm",
    private String reserveTimeZone; //


}
