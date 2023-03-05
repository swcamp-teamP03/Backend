package com.example.swcamp_p03.customerGroup.repository.history;

import com.example.swcamp_p03.customerGroup.entity.history.ExcelDataHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExcelDataHistoryRepository extends JpaRepository<ExcelDataHistory, Long>,ExcelDataHistoryRepositoryCustom {
}
