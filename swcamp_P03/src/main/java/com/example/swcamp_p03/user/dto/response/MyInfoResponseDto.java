package com.example.swcamp_p03.user.dto.response;

import com.example.swcamp_p03.user.entity.User;
import lombok.Getter;

@Getter
public class MyInfoResponseDto {
    private String company;
    private String companyNumber;
    private String username;
    private String phoneNumber;
    private String email;
    private String callingNumber;
    private String callRejectionNumber;

    public MyInfoResponseDto(User user) {
        this.company = user.getCompany();
        this.companyNumber = user.getCompanyNumber();
        this.username = user.getUsername();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.callingNumber = user.getCallingNumber();
        this.callRejectionNumber = user.getCallRejectionNumber();
    }
}
