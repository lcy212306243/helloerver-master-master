package com.stu.helloserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_info")   // 显式指定表名
public class UserInfo {
    private Long userId;       // 对应 user_id，MyBatis-Plus 默认会映射下划线
    private String realName;   // real_name
    private String phone;
    private String address;
}