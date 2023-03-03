package com.example.swcamp_p03.customerGroup.controller;

import com.example.swcamp_p03.common.dto.ResponseDto;
import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.customerGroup.dto.reponse.DetailGroupResponseDto;
import com.example.swcamp_p03.customerGroup.dto.reponse.TotalGroupResponseDto;
import com.example.swcamp_p03.customerGroup.dto.request.FileDownloadRequestDto;
import com.example.swcamp_p03.customerGroup.dto.request.GroupWriteRequestDto;
import com.example.swcamp_p03.customerGroup.service.GroupReadService;
import com.example.swcamp_p03.customerGroup.service.GroupWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
public class CustomerGroupController {
    private final GroupWriteService groupWriteService;
    private final GroupReadService groupReadService;

    @GetMapping("/groups")
    public ResponseDto<TotalGroupResponseDto> getTotalGroup(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return groupReadService.getTotalGroup(pageable);
    }

    @GetMapping("/groups/{groupId}")
    public ResponseDto<DetailGroupResponseDto> getDetailGroup(@PathVariable Long groupId) {
        return groupReadService.getDetailGroup(groupId);
    }

    @PostMapping("/groups")
    public void groupWrite(@AuthenticationPrincipal UserDetailsImpl userDetails, @ModelAttribute GroupWriteRequestDto requestDto) throws IOException {
        groupWriteService.groupWrite(userDetails,requestDto);
    }

    @PutMapping("/groups/{groupId}")
    public void groupUpdate(@PathVariable Long groupId, @ModelAttribute GroupWriteRequestDto requestDto) throws IOException {
        groupWriteService.groupUpdate(groupId, requestDto);
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
