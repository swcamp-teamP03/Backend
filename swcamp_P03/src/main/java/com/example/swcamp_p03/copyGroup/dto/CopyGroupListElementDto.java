package com.example.swcamp_p03.copyGroup.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CopyGroupListElementDto {
    private Long copyId;
    private String createDate;
    private Boolean favorite;
    private String copyName;

    public CopyGroupListElementDto(Long copyId, LocalDateTime createDate, Boolean like, String copyName) {
        this.copyId = copyId;
        this.createDate = createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.favorite = like;
        this.copyName = copyName;
    }
}
