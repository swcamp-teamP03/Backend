package com.example.swcamp_p03.copyGroup.entity;

import com.example.swcamp_p03.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CopyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long copyGroupId;
    private String coupGroupName;
    private String tag;
    private String brandName;
    private String productName;
    private String keyword;
    private String copyType;
    private Boolean favorite;
    private Integer createCount;
    private Integer copyLength;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

}
