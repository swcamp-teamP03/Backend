package com.example.swcamp_p03.campaign.dto.api;

import lombok.Data;

@Data
public class NaverApiMessageResultDto {
    private String requestId;
    private String messageId;
    private String requestTime;
    private String contentType;
    private String type;
    private String countryCode;
    private String from;
    private String to;
    private String completeTime;
    private String telcoCode;
    private String status;
    private String statusCode;
    private String statusName;
    private String statusMessage;
}
