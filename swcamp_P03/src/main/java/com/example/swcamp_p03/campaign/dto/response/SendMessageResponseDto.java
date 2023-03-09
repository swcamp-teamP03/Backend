package com.example.swcamp_p03.campaign.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SendMessageResponseDto {

    private int ListCount;
    private List<SendMessageElementDto> sendList;

}
