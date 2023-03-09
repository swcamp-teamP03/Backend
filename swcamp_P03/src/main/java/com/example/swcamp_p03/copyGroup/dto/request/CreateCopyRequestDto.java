package com.example.swcamp_p03.copyGroup.dto.request;

import lombok.Data;

@Data
public class CreateCopyRequestDto {

    private String brandName;
    private String productName;
    private String keyword;
    private String type;
    private int createCount;
    private int copyLength;
    private String sector;
}
