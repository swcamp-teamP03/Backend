package com.example.swcamp_p03.copyGroup.dto;

import com.example.swcamp_p03.copyGroup.entity.GptCopy;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GptCopyDto {
    private Long copyId;
    private String content;
    private Boolean isPinned;
    private LocalDateTime createDate;


    public GptCopyDto(GptCopy gptCopy) {
        copyId = gptCopy.getGptCopyId();
        content = gptCopy.getContent();
        createDate = gptCopy.getCreatedAt();
        isPinned  = gptCopy.getPin();
    }
}
