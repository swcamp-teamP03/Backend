package com.example.swcamp_p03.copyGroup.dto;

import com.example.swcamp_p03.copyGroup.entity.CopyGroup;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CopyGroupListElementDto {
    private Long copyId;
    private LocalDateTime createDate;
    private Boolean like;
    private String copyName;

    public CopyGroupListElementDto(CopyGroup e) {
        this.copyId = e.getCopyGroupId();
        this.createDate = e.getCreatedAt();
        this.like = e.getFavorite();
        this.copyName = e.getCoupGroupName();
    }
}
