package com.example.swcamp_p03.customerGroup.repository;

import com.example.swcamp_p03.customerGroup.dto.reponse.DetailGroupResponseDto;
import com.example.swcamp_p03.customerGroup.dto.reponse.TotalGroupResponseDto;
import com.example.swcamp_p03.customerGroup.dto.SearchDto;
import com.example.swcamp_p03.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface CustomerGroupRepositoryCustom {
    TotalGroupResponseDto findTotalGroup(User user, Pageable pageable);

    DetailGroupResponseDto findDetailGroup(Long groupId);

    TotalGroupResponseDto findSearch(SearchDto searchDto);
}
