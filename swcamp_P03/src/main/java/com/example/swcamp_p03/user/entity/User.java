package com.example.swcamp_p03.user.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
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
    private Long campaignTestCount;
    private LocalDateTime createdAt;
    private LocalDateTime visitedTime;

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
        this.createdAt = LocalDateTime.now();
        this.campaignTestCount = 0L;
    }
}
