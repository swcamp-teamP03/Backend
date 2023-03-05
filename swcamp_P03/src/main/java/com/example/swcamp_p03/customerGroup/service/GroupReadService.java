package com.example.swcamp_p03.customerGroup.service;

import com.example.swcamp_p03.common.dto.ResponseDto;
import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.customerGroup.dto.reponse.DetailGroupResponseDto;
import com.example.swcamp_p03.customerGroup.dto.reponse.TotalGroupResponseDto;
import com.example.swcamp_p03.customerGroup.repository.CustomerGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupReadService {
    private final CustomerGroupRepository customerGroupRepository;

    // group list
    public ResponseDto<TotalGroupResponseDto> getTotalGroup(UserDetailsImpl userDetails, Pageable pageable) {
        TotalGroupResponseDto totalGroup = customerGroupRepository.findTotalGroup(userDetails.getUser(), pageable);
        return ResponseDto.success(totalGroup);
    }

    // group detail
    public ResponseDto<DetailGroupResponseDto> getDetailGroup(Long groupId) {
        DetailGroupResponseDto detailGroup = customerGroupRepository.findDetailGroup(groupId);
        return ResponseDto.success(detailGroup);
    }
}
