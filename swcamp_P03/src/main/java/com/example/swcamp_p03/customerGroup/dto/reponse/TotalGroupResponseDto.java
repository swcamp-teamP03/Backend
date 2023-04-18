package com.example.swcamp_p03.customerGroup.dto.reponse;

import com.example.swcamp_p03.customerGroup.dto.GroupListDto;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Getter
public class TotalGroupResponseDto {
    private Long totalGroupCount;
    private int totalGroupPage;
    private List<GroupListDto> groupList;

    public TotalGroupResponseDto(Long totalGroupCount, PageImpl<GroupListDto> groupListList) {
        this.totalGroupCount = totalGroupCount;
        this.totalGroupPage = groupListList.getTotalPages();
        this.groupList = groupListList.getContent();
    }
}
