package com.example.swcamp_p03.copyGroup.repository.history;

import com.example.swcamp_p03.copyGroup.entity.history.CopyGroupHistory;
import com.example.swcamp_p03.copyGroup.entity.history.GptCopyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GptCopyHistoryRepository extends JpaRepository<GptCopyHistory,Long> {
}
