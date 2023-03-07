package com.example.swcamp_p03.customerGroup.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class GroupWriteRequestDto {
    private String groupName;
    private List<PropertyDto> properties;
    private String fileOrgName;
    private MultipartFile file;
}
