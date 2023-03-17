package com.example.swcamp_p03.copyGroup.dto.response;

import com.example.swcamp_p03.copyGroup.dto.CopyGroupListElementDto;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Getter
public class CopyGroupListResponseDto {
    private int totalCopy;
    private Long totalCopyCount;
    private List<CopyGroupListElementDto> GroupList;

    public CopyGroupListResponseDto(Long totalCopyCount, PageImpl<CopyGroupListElementDto> copyGroupList) {
        this.totalCopyCount = totalCopyCount;
        this.totalCopy = copyGroupList.getTotalPages();
        this.GroupList = copyGroupList.getContent();
    }
}
