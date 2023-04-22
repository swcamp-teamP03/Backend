package com.example.swcamp_p03.user.repository;

import com.example.swcamp_p03.user.dto.UserDataDto;

import java.util.List;

public interface UserRepositoryCustom {
    List<UserDataDto> getUserDataAll();
}
