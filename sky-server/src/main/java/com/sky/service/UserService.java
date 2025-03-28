package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

public interface UserService {
    /**
     * 用户登陆
     * @return
     */
    User login(UserLoginDTO userLoginDTO);
}
