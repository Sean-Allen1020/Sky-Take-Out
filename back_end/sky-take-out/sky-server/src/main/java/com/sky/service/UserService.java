package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.Category;
import com.sky.entity.User;

import java.util.List;

public interface UserService {

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
