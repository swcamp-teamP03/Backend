package com.example.swcamp_p03.copyGroup.entity.history;

import com.example.swcamp_p03.copyGroup.entity.CopyGroup;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GptCopyHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gptCopyHistoryId;
    private String content;
    private String state;
    private Boolean pin;
    private LocalDateTime updateAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COPY_GROUP_HISTORY_ID")
    private CopyGroupHistory copyGroupHistory;

    public GptCopyHistory(String content, String state, CopyGroupHistory copyGroupHistory, Boolean pin, LocalDateTime updateAt) {
        this.content = content;
        this.state = state;
        this.copyGroupHistory = copyGroupHistory;
        this.pin = pin;
        this.updateAt = updateAt;
    }


}
