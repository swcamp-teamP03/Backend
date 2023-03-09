package com.example.swcamp_p03.campaign.dto;

import lombok.Getter;

@Getter
public class CopyWriteABDto {
    public String copyType;
    public String content;

    public CopyWriteABDto(String copyType, String content) {
        this.copyType = copyType;
        this.content = content;
    }
}
