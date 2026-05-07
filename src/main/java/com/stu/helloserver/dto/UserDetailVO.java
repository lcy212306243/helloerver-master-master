package com.stu.helloserver.dto;

import java.io.Serializable;

public class UserDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String username;
    private String realName;
    private String phone;
    private String address;

    // ========== 手动 Getter / Setter ==========
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}