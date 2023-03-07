package com.example.swcamp_p03.customerGroup.dto.reponse;

import com.example.swcamp_p03.customerGroup.dto.ExcelFileDto;
import com.example.swcamp_p03.customerGroup.dto.GroupCampaignDto;
import com.example.swcamp_p03.customerGroup.dto.GroupPropertyDto;
import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class DetailGroupResponseDto {
    private String groupName;
    private List<GroupPropertyDto> customerProperties;
    private ExcelFileDto excelFile;
    private List<GroupCampaignDto> campaigns;

    public DetailGroupResponseDto(CustomerGroup customerGroup, ExcelFileDto excelFile, List<GroupCampaignDto> campaignDtoList) {
        this.groupName = customerGroup.getCustomerGroupName();
        this.customerProperties = customerGroup.getPropertyList().stream()
                .map(GroupPropertyDto::new)
                .collect(Collectors.toList());
        this.excelFile = excelFile;
        this.campaigns = campaignDtoList;
    }
}
