package com.example.swcamp_p03.copyGroup.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CopyGroupCreateDto {
    private String copyGroupName;
    private String tag;
    private String brandName;
    private String productName;
    private String keyword;
    private String type;
    private String createCount;
    private String copyLength;
    private List<GptCopyDto> copyList = new ArrayList<>();
}
