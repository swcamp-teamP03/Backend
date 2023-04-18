package com.example.swcamp_p03.customerGroup.repository;

import com.example.swcamp_p03.customerGroup.entity.CustomerProperty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerPropertyRepository extends JpaRepository<CustomerProperty,Long> {
}
