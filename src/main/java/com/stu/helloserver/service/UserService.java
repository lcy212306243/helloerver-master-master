package com.stu.helloserver.service;

import com.stu.helloserver.common.Result;
import com.stu.helloserver.dto.UserDTO;
import com.stu.helloserver.dto.UserDetailVO;

public interface UserService {
    Result<UserDetailVO> getUserDetail(Long userId);
    Result<String> updateUserInfo(Long userId, UserDetailVO userDetailVO);
    Result<String> deleteUser(Long userId);
    Result<String> register(UserDTO userDTO);
}