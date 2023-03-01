package com.example.swcamp_p03.customerGroup.repository;

import com.example.swcamp_p03.customerGroup.entity.ExcelFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExcelFileRepository extends JpaRepository<ExcelFile,Long> {
}
