package com.example.swcamp_p03.copyGroup.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateCopyResponseDto {

    List<CreateCopyContent> resultlist = new ArrayList<>();

    public void addCopy(String content){
        resultlist.add(new CreateCopyContent(content));
    }

}




