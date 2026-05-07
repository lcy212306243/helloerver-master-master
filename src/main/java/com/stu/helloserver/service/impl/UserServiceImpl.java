package com.stu.helloserver.service.impl;

import cn.hutool.json.JSONUtil;
import com.stu.helloserver.common.Result;
import com.stu.helloserver.common.ResultCode;
import com.stu.helloserver.dto.UserDTO;
import com.stu.helloserver.dto.UserDetailVO;
import com.stu.helloserver.mapper.UserInfoMapper;
import com.stu.helloserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final StringRedisTemplate redisTemplate;
    private final UserInfoMapper userInfoMapper;

    // 手动构造器注入
    public UserServiceImpl(StringRedisTemplate redisTemplate, UserInfoMapper userInfoMapper) {
        this.redisTemplate = redisTemplate;
        this.userInfoMapper = userInfoMapper;
    }

    private static final String CACHE_KEY_PREFIX = "user:detail:";

    @Override
    public Result<UserDetailVO> getUserDetail(Long userId) {
        String key = CACHE_KEY_PREFIX + userId;
        String json = redisTemplate.opsForValue().get(key);
        if (json != null && !json.isEmpty()) {
            try {
                UserDetailVO cacheVO = JSONUtil.toBean(json, UserDetailVO.class);
                return Result.success(cacheVO);
            } catch (Exception e) {
                log.warn("缓存解析失败，删除 key: {}", key, e);
                redisTemplate.delete(key);
            }
        }

        UserDetailVO detail = userInfoMapper.getUserDetail(userId);
        if (detail == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(detail), 10, TimeUnit.MINUTES);
        return Result.success(detail);
    }

    @Override
    @Transactional
    public Result<String> updateUserInfo(Long userId, UserDetailVO userDetailVO) {
        String key = CACHE_KEY_PREFIX + userId;
        redisTemplate.delete(key);
        return Result.success("更新成功");
    }

    @Override
    @Transactional
    public Result<String> deleteUser(Long userId) {
        String key = CACHE_KEY_PREFIX + userId;
        redisTemplate.delete(key);
        return Result.success("删除成功");
    }

    @Override
    @Transactional
    public Result<String> register(UserDTO userDTO) {
        return Result.success("注册成功");
    }
}