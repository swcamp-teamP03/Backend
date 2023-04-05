package com.example.swcamp_p03.user.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailConfirm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String certificationNumber;
    private LocalDateTime createdAt;

    public MailConfirm(String email, String certificationNumber, LocalDateTime createdAt) {
        this.email = email;
        this.certificationNumber = certificationNumber;
        this.createdAt = createdAt;
    }
}
