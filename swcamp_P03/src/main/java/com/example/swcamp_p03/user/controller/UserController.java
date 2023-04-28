package com.example.swcamp_p03.user.controller;

import com.example.swcamp_p03.common.dto.ResponseDto;
import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.user.dto.MessageDto;
import com.example.swcamp_p03.user.dto.request.RequestLogin;
import com.example.swcamp_p03.user.dto.response.MyInfoResponseDto;
import com.example.swcamp_p03.user.service.EmailService;
import com.example.swcamp_p03.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/sign")
    public void signup(@Valid @RequestBody RequestLogin requestLogin) {
        userService.signup(requestLogin);
    }

    @GetMapping("/mypage/{userId}")
    public ResponseDto<MyInfoResponseDto> myPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getMyInfo(userDetails.getUser());
    }

    @PostMapping("/sign/sendMail")
    public void sendMail(@RequestParam("email") String email) throws Exception {
        emailService.sendSimpleMessage(email);
    }

    @PostMapping("/sign/mailConfirm")
    public ResponseDto<MessageDto> mailConfirm(@RequestParam("email") String email,
                                               @RequestParam("certificationNumber") String certificationNumber) {
        return emailService.mailConfirm(email, certificationNumber);
    }

}
