package com.example.swcamp_p03.customGroup.jpa.testEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ATableRepository extends JpaRepository<AaaTable,Long> {
    AaaTable findByName(String name);
}
