package com.example.swcamp_p03.customerGroup.dto;

import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class GroupListDto {
    private Long customerGroupId;
    private String groupName;
    private Long customerCnt;
    private Boolean favorite;
    private String date;

    // querydsl
    public GroupListDto(CustomerGroup customerGroup, Long customerCnt) {
        this.customerGroupId = customerGroup.getCustomerGroupId();
        this.groupName = customerGroup.getCustomerGroupName();
        this.customerCnt = customerCnt;
        this.favorite = customerGroup.getFavorite();
//        this.date = customerGroup.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
