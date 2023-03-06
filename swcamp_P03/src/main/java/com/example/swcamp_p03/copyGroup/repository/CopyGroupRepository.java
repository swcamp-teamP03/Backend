package com.example.swcamp_p03.copyGroup.repository;

import com.example.swcamp_p03.copyGroup.entity.CopyGroup;
import com.example.swcamp_p03.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CopyGroupRepository extends JpaRepository<CopyGroup,Long> {
    List<CopyGroup> findAllByUser(User user, Pageable pageable);
    List<CopyGroup> findAllByFavorite(Boolean favorite, Pageable pageable);
}
