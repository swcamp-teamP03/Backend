package com.example.swcamp_p03.copyGroup.dto;

import com.example.swcamp_p03.copyGroup.entity.GptCopy;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GptCopyDto {
    private String copyId;
    private String content;
    private Boolean pin;
    private LocalDateTime createDate;


    public GptCopyDto(GptCopy gptCopy) {
        copyId = gptCopy.getGptCopyId().toString();
        content = gptCopy.getContent();
        createDate = gptCopy.getCreatedAt();
        pin = gptCopy.getPin();
    }
}
