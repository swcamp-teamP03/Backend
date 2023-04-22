package com.example.swcamp_p03.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class UserDataDto {
    private String createdAt;
    private String visitedTime;
    private String email;
    private String username;
    private String phoneNumber;
    private String company;
    private String copyCount;
    private String campaignTestCount;

    public UserDataDto(LocalDateTime createdAt, LocalDateTime visitedTime, String email, String username, String phoneNumber, String company, Long copyCount, Long campaignCount) {
        this.createdAt = createdAt == null ? null : createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        this.visitedTime = visitedTime == null ? null : visitedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.company = company;
        this.copyCount = String.valueOf(copyCount);
        this.campaignTestCount = String.valueOf(campaignCount);
    }
}
