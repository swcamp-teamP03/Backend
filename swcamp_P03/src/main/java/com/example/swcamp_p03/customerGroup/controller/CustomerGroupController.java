package com.example.swcamp_p03.customerGroup.controller;

import com.example.swcamp_p03.common.dto.ResponseDto;
import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.customerGroup.dto.reponse.DetailGroupResponseDto;
import com.example.swcamp_p03.customerGroup.dto.reponse.TotalGroupResponseDto;
import com.example.swcamp_p03.customerGroup.dto.request.FileDownloadRequestDto;
import com.example.swcamp_p03.customerGroup.dto.request.GroupWriteRequestDto;
import com.example.swcamp_p03.customerGroup.dto.SearchDto;
import com.example.swcamp_p03.customerGroup.service.GroupReadService;
import com.example.swcamp_p03.customerGroup.service.GroupWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class CustomerGroupController {
    private final GroupWriteService groupWriteService;
    private final GroupReadService groupReadService;

    @GetMapping("/groups")
    public ResponseDto<TotalGroupResponseDto> getTotalGroup(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                                            Pageable pageable) {
        return groupReadService.getTotalGroup(userDetails, pageable);
    }

    @GetMapping("/groups/{groupId}")
    public ResponseDto<DetailGroupResponseDto> getDetailGroup(@PathVariable Long groupId) {
        return groupReadService.getDetailGroup(groupId);
    }

    @GetMapping("/groups/search")
    public ResponseDto<TotalGroupResponseDto> getSearch(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestParam("keyword") String keyword,
                                                        @RequestParam(value = "startDate",required = false,  defaultValue = "1900-01-01")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                        @RequestParam(value = "endDate",required = false,  defaultValue = "2300-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                        Pageable pageable
    ) {
        SearchDto searchDto = SearchDto.builder()
                .user(userDetails.getUser())
                .search(keyword)
                .startDate(startDate)
                .endDate(endDate)
                .pageable(pageable)
                .build();
        return groupReadService.getSearch(searchDto);
    }

    @PostMapping("/groups")
    public void groupWrite(@AuthenticationPrincipal UserDetailsImpl userDetails, @ModelAttribute GroupWriteRequestDto requestDto) throws IOException {
        System.out.println(requestDto);
        groupWriteService.groupWrite(userDetails, requestDto);
    }

    @PutMapping("/groups/{groupId}")
    public void groupUpdate(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails, @ModelAttribute GroupWriteRequestDto requestDto) throws IOException {
        groupWriteService.groupUpdate(groupId, requestDto, userDetails);
    }

    @PostMapping("/groups/{groupId}/file/download")
    public ResponseEntity<Resource> fileDownload(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody FileDownloadRequestDto requestDto) throws MalformedURLException {
        return groupWriteService.fileDownload(groupId, userDetails, requestDto);
    }

    @PostMapping("/groups/{groupId}")
    public void favorite(@PathVariable Long groupId) {
        groupWriteService.favoriteCheck(groupId);
    }
}
