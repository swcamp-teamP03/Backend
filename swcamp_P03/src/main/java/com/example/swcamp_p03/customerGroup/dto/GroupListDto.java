package com.example.swcamp_p03.customerGroup.dto;

import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GroupListDto {
    private Long customerGroupId;
    private String groupName;
    private Long customerCnt;
    private Boolean favorite;
    private LocalDateTime date;

    public GroupListDto(CustomerGroup customerGroup, Long customerCnt) {
        this.customerGroupId = customerGroup.getCustomerGroupId();
        this.groupName = customerGroup.getCustomerGroupName();
        this.customerCnt = customerCnt;
        this.favorite = customerGroup.getFavorite();
        this.date = customerGroup.getCreatedAt();
    }
}
