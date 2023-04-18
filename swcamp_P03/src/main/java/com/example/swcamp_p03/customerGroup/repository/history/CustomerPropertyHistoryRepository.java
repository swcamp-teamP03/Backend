package com.example.swcamp_p03.customerGroup.repository.history;

import com.example.swcamp_p03.customerGroup.entity.history.CustomerPropertyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerPropertyHistoryRepository extends JpaRepository<CustomerPropertyHistory,Long> {
}
