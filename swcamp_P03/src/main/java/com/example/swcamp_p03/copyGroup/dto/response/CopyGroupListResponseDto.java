package com.example.swcamp_p03.copyGroup.dto.response;

import com.example.swcamp_p03.copyGroup.dto.CopyGroupListElementDto;
import lombok.Getter;

import java.util.List;

@Getter
public class CopyGroupListResponseDto {
    private String totalCopy;
    private List<CopyGroupListElementDto> copyList;

    public CopyGroupListResponseDto(int totalCopy, List<CopyGroupListElementDto> copyList) {
        this.totalCopy = String.valueOf(totalCopy);
        this.copyList = copyList;
    }
}
