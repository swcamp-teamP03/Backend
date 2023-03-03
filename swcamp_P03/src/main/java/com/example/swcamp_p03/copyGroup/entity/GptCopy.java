package com.example.swcamp_p03.copyGroup.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GptCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gptCopyId;
    private Long copyGroupId;
    private String content;
    private String state;
    private Boolean pin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COPY_GROUP")
    private CopyGroup copyGroup;

    public GptCopy(String content, String state, CopyGroup copyGroup, Boolean pin) {
        this.content = content;
        this.state = state;
        this.copyGroup = copyGroup;
        this.pin = pin;
    }

    public void report(){
        state = "reported";
    }
}
