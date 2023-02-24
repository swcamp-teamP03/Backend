package com.example.swcamp_p03.user.controller;

import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.user.domain.User;
import com.example.swcamp_p03.user.dto.RequestLogin;
import com.example.swcamp_p03.user.service.UserService;
import com.example.swcamp_p03.util.dto.ResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign")
    public ResponseDto<UserDto> signup(@RequestBody RequestLogin requestLogin) {
        return userService.signup(requestLogin);
    }

    @GetMapping("/user/test")
    public UserDetailsImpl test(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userDetails;
    }

    @Getter
    public static class UserDto {
        private String username;
        private String password;

        public UserDto(User user) {
            this.username = user.getUsername();
            this.password = user.getPassword();
        }
    }

}
