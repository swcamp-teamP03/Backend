package com.example.swcamp_p03.customerGroup.dto.reponse;

import com.example.swcamp_p03.customerGroup.dto.GroupListDto;
import lombok.Getter;

import java.util.List;

@Getter
public class TotalGroupResponseDto {
    private int totalGroupPage;
    private List<GroupListDto> groupListList;

    public TotalGroupResponseDto(int count, List<GroupListDto> groupListList) {
        this.totalGroupPage = count;
        this.groupListList = groupListList;
    }
}
