package com.example.swcamp_p03.campaign.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SendMessageElementDto {

    private Long sendMessageId;
    private LocalDateTime sendDate;
    private String name;
    private String phone;
    private String state;
    private String errorMessage;

}
