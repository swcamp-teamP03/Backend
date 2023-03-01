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

    @ManyToOne
    @JoinColumn(name = "COPY_GROUP")
    private CopyGroup copyGroup;
}
