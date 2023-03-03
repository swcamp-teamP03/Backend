package com.example.swcamp_p03.customerGroup.repository;

import com.example.swcamp_p03.customerGroup.dto.reponse.DetailGroupResponseDto;
import com.example.swcamp_p03.customerGroup.dto.reponse.TotalGroupResponseDto;
import org.springframework.data.domain.Pageable;

public interface CustomerGroupRepositoryCustom {
    TotalGroupResponseDto findTotalGroup(Pageable pageable);

    DetailGroupResponseDto findDetailGroup(Long groupId);
}
