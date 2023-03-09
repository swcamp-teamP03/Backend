package com.example.swcamp_p03.user.service;

import com.example.swcamp_p03.common.exception.ErrorCode;
import com.example.swcamp_p03.common.exception.GlobalException;
import com.example.swcamp_p03.user.dto.response.MyInfoResponseDto;
import com.example.swcamp_p03.user.entity.User;
import com.example.swcamp_p03.user.dto.request.RequestLogin;
import com.example.swcamp_p03.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signup(RequestLogin requestLogin) {
        Optional<User> findUser = userRepository.findByEmail(requestLogin.getEmail());
        if (!findUser.isPresent()) {
            User user = User.register()
                    .email(requestLogin.getEmail())
                    .password(bCryptPasswordEncoder.encode(requestLogin.getPassword()))
                    .roles("ROLE_USER")
                    .username(requestLogin.getUsername())
                    .phoneNumber(requestLogin.getPhoneNumber())
                    .company(requestLogin.getCompany())
                    .companyNumber(requestLogin.getCompanyNumber())
                    .callingNumber(requestLogin.getCallingNumber())
                    .callRejectionNumber(requestLogin.getCallRejectionNumber())
                    .build();
            userRepository.save(user);
        } else {
            throw new GlobalException(ErrorCode.EXIST_EMAIL);
        }
    }

    public MyInfoResponseDto getMyInfo(User user) {
        User findUser = userRepository.findByEmail(user.getEmail()).get();
        return new MyInfoResponseDto(findUser);
    }
}
