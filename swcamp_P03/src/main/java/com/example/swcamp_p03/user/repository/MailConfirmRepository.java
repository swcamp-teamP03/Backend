package com.example.swcamp_p03.user.repository;

import com.example.swcamp_p03.user.entity.MailConfirm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailConfirmRepository extends JpaRepository<MailConfirm,Long> {
    Optional<MailConfirm> findByEmail(String to);
}
