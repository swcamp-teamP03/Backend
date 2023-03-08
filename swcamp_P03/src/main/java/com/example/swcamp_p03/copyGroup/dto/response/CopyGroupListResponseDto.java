package com.example.swcamp_p03.copyGroup.dto.response;

import com.example.swcamp_p03.copyGroup.dto.CopyGroupListElementDto;
import lombok.Getter;

import java.util.List;

@Getter
public class CopyGroupListResponseDto {
    private int totalCopy;
    private List<CopyGroupListElementDto> GroupList;

    public CopyGroupListResponseDto(int totalCopy, List<CopyGroupListElementDto> copyList) {
        this.totalCopy = totalCopy;
        this.GroupList = copyList;
    }
}
