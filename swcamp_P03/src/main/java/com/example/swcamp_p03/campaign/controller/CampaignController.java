package com.example.swcamp_p03.campaign.controller;


import com.example.swcamp_p03.campaign.dto.api.NaverApiMessage;
import com.example.swcamp_p03.campaign.dto.request.CampaignRequestDto;
import com.example.swcamp_p03.campaign.dto.request.CommentRequestDto;
import com.example.swcamp_p03.campaign.dto.request.MessageTestDto;
import com.example.swcamp_p03.campaign.dto.response.CampaignCreateResponseDto;
import com.example.swcamp_p03.campaign.dto.response.CampaignDetailDto;
import com.example.swcamp_p03.campaign.dto.response.SendMessageResponseDto;
import com.example.swcamp_p03.campaign.dto.response.TotalCampaignResponseDto;
import com.example.swcamp_p03.campaign.service.CampaignReadService;
import com.example.swcamp_p03.campaign.service.CampaignService;
import com.example.swcamp_p03.campaign.service.CampaignWriteService;
import com.example.swcamp_p03.common.dto.ResponseDto;
import com.example.swcamp_p03.common.dto.SearchDto;
import com.example.swcamp_p03.common.exception.ErrorCode;
import com.example.swcamp_p03.common.exception.GlobalException;
import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.user.entity.User;
import com.example.swcamp_p03.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CampaignController {
    private final UserRepository userRepository;
    private final CampaignService campaignService;
    private final CampaignReadService campaignReadService;
    private final CampaignWriteService campaignWriteService;

    //    @PostMapping("/campaign")
    public CampaignCreateResponseDto createCampaign(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CampaignRequestDto requestDto) throws Exception {
        Long campaignId = campaignService.createCampaign(userDetails.getUser(), requestDto);
        return new CampaignCreateResponseDto(campaignId, "success");
    }

    @PostMapping("/campaign")
    public ResponseDto<String> createCampaignTestVersion(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CampaignRequestDto requestDto) throws Exception {

        if (userDetails.getUser().getPhoneNumber() == null) {
            throw new GlobalException(ErrorCode.PHONE_NUMBER_NOT_REGISTERED);
        }

        if (requestDto.getSendingDate() != null && campaignService.checkTime15M(LocalDateTime.parse(requestDto.getSendingDate()))) {
            log.info("checkTime15M (calling setSendingDate(LocalDateTime.now().toString())) : 발송예약은 현재시간으로부터 15분 후부터 가능(따라서 즉시발송으로 변경)");
            requestDto.setSendingDate(null);
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (requestDto.getSendingDate() != null) {
            LocalDateTime parse = LocalDateTime.parse(requestDto.getSendingDate());
            requestDto.setSendingDate(parse.format(dateTimeFormatter));
        }

        String phoneNum = userDetails.getUser().getPhoneNumber().replace("-", "");
        ArrayList<NaverApiMessage> apiMessages = new ArrayList<>();
        apiMessages.add(new NaverApiMessage(phoneNum, "copyt 테스트 발송문자", "(광고)" + requestDto.getMessageA() + " [이부분에 링크가 들어갑니다.]"));
        if (requestDto.getMessageB() != null && !requestDto.getMessageB().equals("null")) {
            apiMessages.add(new NaverApiMessage(phoneNum, "copyt 테스트 발송문자", "(광고)" + requestDto.getMessageB() + " [이부분에 링크가 들어갑니다.]"));
        }
        campaignService.naverApiLmsSend(apiMessages, requestDto.getSendingDate(), "COMM");

        User user = userDetails.getUser();
        user.setCampaignTestCount(user.getCampaignTestCount() + 1);
        userRepository.save(user);

        return ResponseDto.success("성공");
    }

    @GetMapping("/campaigns/{campaignId}/sendmessages")
    public ResponseDto<SendMessageResponseDto> getSendMessages(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long campaignId) throws Exception {
        SendMessageResponseDto sendMessages = campaignService.getSendMessages(userDetails.getUser(), campaignId);
        return ResponseDto.success(sendMessages);
    }

    @PostMapping("/message")
    public ResponseDto<String> testSendMessage(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody MessageTestDto message) throws Exception {
        if (userDetails.getUser().getPhoneNumber() == null) {
            throw new GlobalException(ErrorCode.PHONE_NUMBER_NOT_REGISTERED);
        }
        String phoneNum = userDetails.getUser().getPhoneNumber().replace("-", "");
        ArrayList<NaverApiMessage> apiMessages = new ArrayList<>();
        apiMessages.add(new NaverApiMessage(phoneNum, "copyt 테스트 발송문자", "(광고)" + message.getMessage() + " [이부분에 링크가 들어갑니다.]"));
        campaignService.naverApiLmsSend(apiMessages, null, "COMM");
        return ResponseDto.success("성공");
    }


    @GetMapping("/campaigns")
    public ResponseDto<TotalCampaignResponseDto> getTotalCampaign(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                                                  Pageable pageable) {
        return campaignReadService.getTotalCampaign(userDetails.getUser(), pageable);
    }

    @GetMapping("/campaigns/search")
    public ResponseDto<TotalCampaignResponseDto> getSearch(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestParam("keyword") String keyword,
                                                           @RequestParam(value = "startDate", required = false, defaultValue = "1900-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                           @RequestParam(value = "endDate", required = false, defaultValue = "2300-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                           Pageable pageable) {
        SearchDto searchDto = SearchDto.builder()
                .user(userDetails.getUser())
                .search(keyword)
                .startDate(startDate)
                .endDate(endDate)
                .pageable(pageable)
                .build();
        return campaignReadService.getSearch(searchDto);
    }

    @PostMapping("/campaigns/{campaignId}")
    public void favorite(@PathVariable Long campaignId) {
        campaignWriteService.favoriteCheck(campaignId);
    }

    @PostMapping("/campaigns/{campaignId}/comment")
    public void writeComment(@PathVariable Long campaignId, @RequestBody CommentRequestDto requestDto) {
        campaignWriteService.writeComment(campaignId, requestDto);
    }

    @GetMapping("/campaigns/{campaignId}")
    public ResponseDto<CampaignDetailDto> getDetailCampaign(@PathVariable Long campaignId) throws Exception {
        return campaignReadService.getDetailCampaign(campaignId);
    }

}
