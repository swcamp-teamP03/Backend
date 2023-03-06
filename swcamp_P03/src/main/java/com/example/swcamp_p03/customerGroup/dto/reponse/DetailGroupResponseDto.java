package com.example.swcamp_p03.customerGroup.dto.reponse;

import com.example.swcamp_p03.customerGroup.dto.ExcelFileDto;
import com.example.swcamp_p03.customerGroup.dto.GroupPropertyDto;
import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DetailGroupResponseDto {
    private String groupName;
    private List<GroupPropertyDto> customerProperties;
    private ExcelFileDto excelFileDto;

    public DetailGroupResponseDto(CustomerGroup customerGroup, ExcelFileDto excelFileDto) {
        this.groupName = customerGroup.getCustomerGroupName();
        this.customerProperties = customerGroup.getPropertyList().stream()
                .map(GroupPropertyDto::new)
                .collect(Collectors.toList());
        this.excelFileDto = excelFileDto;
    }
}
