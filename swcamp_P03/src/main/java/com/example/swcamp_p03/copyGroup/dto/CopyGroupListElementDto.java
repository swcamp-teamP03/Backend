package com.example.swcamp_p03.copyGroup.dto;

import com.example.swcamp_p03.copyGroup.entity.CopyGroup;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CopyGroupListElementDto {
    private Long copyId;
    private String createDate;
    private Boolean favorite;
    private String copyName;

//    public CopyGroupListElementDto(CopyGroup e) {
//        this.copyId = e.getCopyGroupId();
//        this.createDate = e.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        this.like = e.getFavorite();
//        this.copyName = e.getCoupGroupName();
//    }

    public CopyGroupListElementDto(Long copyId, LocalDateTime createDate, Boolean like, String copyName) {
        this.copyId = copyId;
        this.createDate = createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.favorite = like;
        this.copyName = copyName;
    }
}
