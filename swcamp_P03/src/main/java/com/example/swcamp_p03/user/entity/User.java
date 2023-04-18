package com.example.swcamp_p03.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Table(name = "MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String username;
    private String password;
    private String roles;
    private String company;
    private String phoneNumber;
    private String companyNumber;
    private String callingNumber;
    private String callRejectionNumber;

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    @Builder(builderClassName = "register",builderMethodName = "register")
    public User(String email, String username, String password, String roles, String company, String phoneNumber, String companyNumber, String callingNumber, String callRejectionNumber) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.company = company;
        this.phoneNumber = phoneNumber;
        this.companyNumber = companyNumber;
        this.callingNumber = callingNumber;
        this.callRejectionNumber = callRejectionNumber;
    }
}
