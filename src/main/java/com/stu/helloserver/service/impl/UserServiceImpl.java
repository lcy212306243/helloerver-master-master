package com.stu.helloserver.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stu.helloserver.common.Result;
import com.stu.helloserver.dto.UserDTO;
import com.stu.helloserver.entity.User;
import com.stu.helloserver.mapper.UserMapper;
import com.stu.helloserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<String> register(UserDTO userDTO) {
        return null;
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        return null;
    }

    @Override
    public Result<String> getUserById(Long id) {
        return null;
    }

    @Override
    public Result<Object> getUserPage(Integer pageNum, Integer pageSize) {
        // 1. 创建分页对象，Page<T> 是 MyBatis-Plus 提供的分页模型
        Page<User> pageParam = new Page<>(pageNum, pageSize);

        // 2. 执行分页查询，第二个参数为查询条件 Wrapper，null 表示无条件
        Page<User> resultPage = userMapper.selectPage(pageParam, null);

        // 3. 返回结果（resultPage 中包含 records、total、current、pages 等字段）
        return Result.success(resultPage);
    }
}