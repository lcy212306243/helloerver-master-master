package com.stu.helloserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stu.helloserver.common.Result;
import com.stu.helloserver.config.JwtUtil;
import com.stu.helloserver.dto.UserDTO;
import com.stu.helloserver.entity.User;
import com.stu.helloserver.mapper.UserMapper;
import com.stu.helloserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Result<String> register(UserDTO userDTO) {
        if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
            return Result.error(500, "用户名不能为空");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            return Result.error(500, "密码不能为空");
        }
        User existUser = userMapper.selectByUsername(userDTO.getUsername());
        if (existUser != null) {
            return Result.error(500, "用户名已存在");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        userMapper.insert(user);
        return Result.success("注册成功");
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        User user = userMapper.selectByUsername(userDTO.getUsername());
        if (user == null) {
            return Result.error(500, "用户名不存在");
        }
        if (!userDTO.getPassword().equals(user.getPassword())) {
            return Result.error(500, "密码错误");
        }
        String jwt = jwtUtil.generateToken(userDTO.getUsername());
        return Result.success(jwt);
    }

    @Override
    public Result<String> getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error(500, "用户不存在");
        }
        return Result.success(user.getUsername());
    }

    @Override
    public Result<Object> getUserPage(Integer pageNum, Integer pageSize) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        userMapper.selectPage(page, new QueryWrapper<>());
        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("pages", page.getPages());
        result.put("current", page.getCurrent());
        result.put("size", page.getSize());
        result.put("records", page.getRecords());
        return Result.success(result);
    }
}