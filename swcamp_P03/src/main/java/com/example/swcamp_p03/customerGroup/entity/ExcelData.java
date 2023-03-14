package com.example.swcamp_p03.customerGroup.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelData {
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String phoneNumber;


    @Builder(builderClassName = "testInsert", builderMethodName = "testInsert")
    public ExcelData(String username, String phoneNumber, ExcelFile excelFile) {
        this.username = username;
        this.phoneNumber = phoneNumber;
    }
}
