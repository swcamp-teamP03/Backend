package com.example.swcamp_p03.copyGroup.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateCopyResponseDto {

    List<CreateCopyContent> resultList = new ArrayList<>();

    public void addCopy(String content){
        resultList.add(new CreateCopyContent(content));
    }

}




