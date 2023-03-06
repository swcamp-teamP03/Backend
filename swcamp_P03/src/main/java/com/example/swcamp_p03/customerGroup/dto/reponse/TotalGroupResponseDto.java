package com.example.swcamp_p03.customerGroup.dto.reponse;

import com.example.swcamp_p03.customerGroup.dto.GroupListDto;
import lombok.Getter;

import java.util.List;

@Getter
public class TotalGroupResponseDto {
    private Long totalGroup;
    private List<GroupListDto> groupListList;

    public TotalGroupResponseDto(Long count, List<GroupListDto> groupListList) {
        this.totalGroup = count;
        this.groupListList = groupListList;
    }
}
