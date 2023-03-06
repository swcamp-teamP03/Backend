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
    private String state;
    private Boolean pin;
    private LocalDateTime updateAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COPY_GROUP_ID")
    private CopyGroup copyGroup;

    public GptCopy(String content, String state, CopyGroup copyGroup, Boolean pin, LocalDateTime updateAt) {
        this.content = content;
        this.state = state;
        this.copyGroup = copyGroup;
        this.pin = pin;
        this.updateAt = updateAt;
    }

    public void report(){
        state = "reported";
    }
}