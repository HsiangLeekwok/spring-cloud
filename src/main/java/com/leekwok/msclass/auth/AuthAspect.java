package com.leekwok.msclass.auth;

import com.leekwok.msclass.jwt.JwtOperator;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <b>Author</b>: Xiang Liguo<br/>
 * <b>Date</b>: 2020/02/21 15:17<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
@Order(1)
@Aspect
@Component
public class AuthAspect {

    @Autowired
    private JwtOperator jwtOperator;

    public boolean validateToken(String token) {
        return jwtOperator.validateToken(token);
    }

    @Around("@annotation(com.leekwok.msclass.auth.Login)")
    public Object checkLogin(ProceedingJoinPoint point) {
        try {
            // 1. 获取 Http 请求中 header 的 token
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert requestAttributes != null;
            HttpServletRequest request = requestAttributes.getRequest();
            String token = request.getHeader("Authorization");
            if (StringUtils.isEmpty(token)) {
                throw new SecurityException("token 不能为空");
            }
            // 2. 校验 token 是否合法，如果合法则认为用户已经登录，反之则返回 401
            if (!validateToken(token)) {
                throw new SecurityException("Token 非法");
            }
            Claims userInfo = this.jwtOperator.getClaimsFromToken(token);
            request.setAttribute("userId", userInfo.get("userId"));
            request.setAttribute("username", userInfo.get("username"));
            return point.proceed();
        } catch (Throwable throwable) {
            throw new SecurityException(throwable);
        }
    }
}
