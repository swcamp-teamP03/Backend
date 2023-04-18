package com.example.swcamp_p03.user.dto.response;

import com.example.swcamp_p03.user.entity.User;
import lombok.Getter;

@Getter
public class MyInfoResponseDto {
    private String email;
    private String username;
    private String phoneNumber;
    private String company;

    public MyInfoResponseDto(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.phoneNumber = user.getPhoneNumber();
        this.company = user.getCompany();
    }
}
