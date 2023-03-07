package com.example.swcamp_p03.copyGroup.dto.response;

import com.example.swcamp_p03.copyGroup.dto.request.CreateCopyContent;
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




