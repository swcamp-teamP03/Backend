package com.example.swcamp_p03.customerGroup.repository.history;

import com.example.swcamp_p03.customerGroup.entity.history.ExcelFileHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExcelFileHistoryRepository extends JpaRepository<ExcelFileHistory,Long> {
}
