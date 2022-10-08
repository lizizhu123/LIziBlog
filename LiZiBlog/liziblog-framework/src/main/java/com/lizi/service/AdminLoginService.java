package com.lizi.service;

import com.lizi.domain.ResponseResult;
import com.lizi.domain.dto.UserDto;

public interface AdminLoginService {
    ResponseResult login(UserDto user);

    ResponseResult logout();

    ResponseResult smsLogin(String phone);
}
