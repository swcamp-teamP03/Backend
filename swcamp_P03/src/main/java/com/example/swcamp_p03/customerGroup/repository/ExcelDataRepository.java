package com.example.swcamp_p03.customerGroup.repository;

import com.example.swcamp_p03.customerGroup.entity.ExcelData;
import com.example.swcamp_p03.customerGroup.entity.ExcelFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExcelDataRepository extends JpaRepository<ExcelData, Long> {

    void deleteByExcelFile(ExcelFile excelFile);
}
