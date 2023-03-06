package com.example.swcamp_p03.copyGroup.dto;

import com.example.swcamp_p03.copyGroup.entity.GptCopy;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GptCopyDto {
    private String copyId;
    private String content;
    private String state;
    private Boolean pin;


    public GptCopyDto(GptCopy gptCopy) {
        copyId = gptCopy.getGptCopyId().toString();
        content = gptCopy.getContent();
        state = gptCopy.getState();
        pin = gptCopy.getPin();
    }
}
