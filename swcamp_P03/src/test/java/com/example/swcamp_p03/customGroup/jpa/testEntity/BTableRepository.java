package com.example.swcamp_p03.customGroup.jpa.testEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BTableRepository extends JpaRepository<BbbTable,Long> {
    List<BbbTable> findAllByAaaTable(AaaTable findA);
}
