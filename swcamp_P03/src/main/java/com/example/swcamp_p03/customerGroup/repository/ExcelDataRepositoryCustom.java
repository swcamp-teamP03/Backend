package com.example.swcamp_p03.customerGroup.repository;

import com.example.swcamp_p03.customerGroup.dto.ExcelDataDto;
import com.example.swcamp_p03.customerGroup.entity.ExcelData;

import java.util.List;

public interface ExcelDataRepositoryCustom {
    void bulkInsert(List<ExcelDataDto> excelDataList);
}

