package com.example.swcamp_p03.copyGroup.dto.request;

import lombok.Data;

@Data
public class CreateCopyRequestDto {

    private String brandName;
    private String productName;
    private String keyword;
    private String type;
    private String createCount;
    private String copyLength;
    private String sector;
}
