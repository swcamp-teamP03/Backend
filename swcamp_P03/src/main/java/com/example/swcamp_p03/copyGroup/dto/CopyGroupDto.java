package com.example.swcamp_p03.copyGroup.dto;

import com.example.swcamp_p03.copyGroup.entity.CopyGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CopyGroupDto {
    private String copyGroupName;
    private String tag;
    private String brandName;
    private String productName;
    private String keyword;
    private String type;
    private String createCount;
    private String copyLength;
    private List<GptCopyDto> copyList = new ArrayList<>();

    public CopyGroupDto(CopyGroup copyGroup) {
        this.copyGroupName = copyGroup.getCoupGroupName();
        this.tag = copyGroup.getTag();
        this.brandName = copyGroup.getBrandName();
        this.productName = copyGroup.getProductName();
        this.keyword = copyGroup.getKeyword();
        this.type = copyGroup.getCopyType();
        this.createCount = copyGroup.getCreateCount().toString();
        this.copyLength = copyGroup.getCopyLength().toString();
    }
}
