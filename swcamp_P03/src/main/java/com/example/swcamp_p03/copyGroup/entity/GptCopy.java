package com.example.swcamp_p03.copyGroup.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GptCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gptCopyId;
    private String content;
    private Boolean pin;
    private LocalDateTime updateAt;
    private LocalDateTime createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COPY_GROUP_ID")
    private CopyGroup copyGroup;

    public GptCopy(String content, CopyGroup copyGroup, Boolean pin,LocalDateTime createdAt, LocalDateTime updateAt) {
        this.content = content;
        this.copyGroup = copyGroup;
        this.pin = pin;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

}
