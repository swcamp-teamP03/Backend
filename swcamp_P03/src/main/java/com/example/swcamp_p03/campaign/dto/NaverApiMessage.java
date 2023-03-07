package com.example.swcamp_p03.campaign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NaverApiMessage {
    private String to;
    private String subject;
    private String content;

}
