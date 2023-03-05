package com.example.swcamp_p03.customerGroup.repository;

import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import com.example.swcamp_p03.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Long>,CustomerGroupRepositoryCustom {
    Optional<CustomerGroup> findByCustomerGroupIdAndUser(Long groupId, User user);
}
