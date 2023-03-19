package com.example.swcamp_p03.copyGroup.dto.response;

import com.example.swcamp_p03.copyGroup.dto.CopyGroupListElementDto;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Getter
public class CopyGroupListResponseDto {
    private Long totalCopyCount;
    private int totalCopy;
    private List<CopyGroupListElementDto> groupList;

    public CopyGroupListResponseDto(Long totalCopyCount, PageImpl<CopyGroupListElementDto> groupList) {
        this.totalCopyCount = totalCopyCount;
        this.totalCopy = groupList.getTotalPages();
        this.groupList = groupList.getContent();
    }
}
