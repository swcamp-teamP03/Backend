package com.example.swcamp_p03.copyGroup.dto.request;

import lombok.Data;

@Data
public class CreateCopyContent {
    String content;

    public CreateCopyContent(String content) {
        this.content = content;
    }
}
