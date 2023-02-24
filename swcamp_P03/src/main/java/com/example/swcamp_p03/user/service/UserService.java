package com.example.swcamp_p03.user.service;

import com.example.swcamp_p03.user.controller.UserController;
import com.example.swcamp_p03.user.domain.User;
import com.example.swcamp_p03.user.dto.RequestLogin;
import com.example.swcamp_p03.user.repository.UserRepository;
import com.example.swcamp_p03.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ResponseDto<UserController.UserDto> signup(RequestLogin requestLogin) {
        User user = User.builder()
                .username(requestLogin.getUsername())
                .password(bCryptPasswordEncoder.encode(requestLogin.getPassword()))
                .roles("ROLE_USER")
                .build();
        userRepository.save(user);
        UserController.UserDto userDto = new UserController.UserDto(user);
        return ResponseDto.success(userDto);
    }
}
