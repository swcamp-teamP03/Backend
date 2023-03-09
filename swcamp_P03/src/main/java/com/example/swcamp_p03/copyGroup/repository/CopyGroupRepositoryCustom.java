package com.example.swcamp_p03.copyGroup.repository;

import com.example.swcamp_p03.copyGroup.dto.CopyGroupListElementDto;
import com.example.swcamp_p03.copyGroup.dto.response.CopyGroupListResponseDto;
import com.example.swcamp_p03.customerGroup.dto.SearchDto;
import com.example.swcamp_p03.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CopyGroupRepositoryCustom {
    CopyGroupListResponseDto findTotalCopyGroup(User user, Pageable pageable);

    CopyGroupListResponseDto findSearch(SearchDto searchDto);
}
