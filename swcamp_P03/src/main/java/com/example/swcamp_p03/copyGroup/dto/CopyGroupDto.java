package com.example.swcamp_p03.copyGroup.dto;

import com.example.swcamp_p03.copyGroup.entity.CopyGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CopyGroupDto {
    private String copyGroupName;
    private String brandName;
    private String productName;
    private String keyword;
    private String type;
    private String sector;
    private int createCount;
    private String targetAge;
    private String targetGender;
    private String createdAt;
    private List<GptCopyDto> copyList = new ArrayList<>();

    public CopyGroupDto(CopyGroup copyGroup) {
        this.copyGroupName = copyGroup.getCoupGroupName();
        this.brandName = copyGroup.getBrandName();
        this.productName = copyGroup.getProductName();
        this.keyword = copyGroup.getKeyword();
        this.type = copyGroup.getCopyType();
        this.sector = copyGroup.getSector();
        this.createCount = copyGroup.getCreateCount();
        this.targetAge = copyGroup.getTargetAge();
        this.targetGender = copyGroup.getTargetGender();
        this.createdAt = copyGroup.getCreatedAt().toString().split("T")[0];
    }
}
