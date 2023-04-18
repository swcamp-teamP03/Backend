package com.example.swcamp_p03.copyGroup.repository.history;

import com.example.swcamp_p03.copyGroup.entity.CopyGroup;
import com.example.swcamp_p03.copyGroup.entity.history.CopyGroupHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CopyGroupHistoryRepository extends JpaRepository<CopyGroupHistory,Long> {
    List<CopyGroupHistory> findByCopyGroup(CopyGroup copyGroup, Pageable pageable);
}
