package com.example.swcamp_p03.customerGroup.repository.history;

import com.example.swcamp_p03.customerGroup.dto.ExcelDataHistoryDto;

import java.util.List;

public interface ExcelDataHistoryRepositoryCustom {
    void bulkInsert(List<ExcelDataHistoryDto> excelDataList);
}
