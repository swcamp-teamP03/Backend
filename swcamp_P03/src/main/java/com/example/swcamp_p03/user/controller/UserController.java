package com.example.swcamp_p03.user.controller;

import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.user.dto.request.RequestLogin;
import com.example.swcamp_p03.user.dto.response.MyInfoResponseDto;
import com.example.swcamp_p03.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign")
    public void signup(@Valid @RequestBody RequestLogin requestLogin) {
        userService.signup(requestLogin);
    }

    @GetMapping("/mypage/{userId}")
    public MyInfoResponseDto myPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getMyInfo(userDetails.getUser());
    }

}
