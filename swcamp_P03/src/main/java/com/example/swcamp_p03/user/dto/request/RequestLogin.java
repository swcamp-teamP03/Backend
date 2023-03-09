package com.example.swcamp_p03.user.dto.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class RequestLogin {
    @NotNull(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;
    private String password;
    private String company;
    private String companyNumber;
    private String username;
    private String phoneNumber;
    private String callingNumber;
    private String callRejectionNumber;
}
