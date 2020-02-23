package com.leekwok.msclass.auth;

import com.leekwok.msclass.jwt.JwtOperator;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <b>Author</b>: Xiang Liguo<br/>
 * <b>Date</b>: 2020/02/21 15:17<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
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
            validationToken();
            return point.proceed();
        } catch (Throwable throwable) {
            throw new SecurityException(throwable);
        }
    }

    @Around("@annotation(com.leekwok.msclass.auth.CheckAuthz)")
    public Object checkAuthz(ProceedingJoinPoint point)  {
        try {
            HttpServletRequest request = validationToken();
            // 2. 判断角色是否可以
            // 拿到 token 里的 role
            Object role = request.getAttribute("role");
            // 拿到 注解里的 role
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            CheckAuthz annotation = method.getAnnotation(CheckAuthz.class);
            String roleInAnnotation = annotation.hasRole();
            if (!Objects.equals(role, roleInAnnotation)) {
                throw new SecurityException("当前用户不具备" + roleInAnnotation + "的角色");
            }
            return point.proceed();
        } catch (Throwable throwable) {
            throw new SecurityException(throwable);
        }
    }

    private HttpServletRequest validationToken() {
        // 1. 校验 token
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
        request.setAttribute("role", userInfo.get("role"));
        return request;
    }
}
