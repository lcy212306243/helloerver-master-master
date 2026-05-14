package com.stu.helloserver.config;

import com.stu.helloserver.entity.User;
import com.stu.helloserver.mapper.UserMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 从请求头中获取 Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. 如果没有 Authorization 或者不是 Bearer 开头，直接放行
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 截取 JWT 字符串
        String jwt = authHeader.substring(7);
        String username;

        try {
            // 4. 从 JWT 中解析用户名
            username = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            // token 解析失败，直接继续后续过滤器（不会认证）
            filterChain.doFilter(request, response);
            return;
        }

        // 5. 如果解析到了用户名，且当前 SecurityContext 中还没有认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 从数据库查询用户信息（包含权限）
            User user = userMapper.selectByUsername(username);
            if (user != null && !jwtUtil.isTokenExpired(jwt)) {
                // 构造 UserDetails 对象（Spring Security 需要）
                // 如果你的 User 实体实现了 UserDetails，可以直接使用；否则简单转换
                UserDetails userDetails = org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())  // 密码已加密
                        .authorities(Collections.emptyList()) // 若没有角色权限，给空集合
                        .build();

                // 创建认证令牌
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 将认证信息放入 SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 继续执行过滤器链
        filterChain.doFilter(request, response);
    }
}