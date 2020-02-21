package com.leekwok.msclass.controller;

import com.leekwok.msclass.auth.Login;
import com.leekwok.msclass.entity.Lesson;
import com.leekwok.msclass.service.LessonService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <b>Author</b>: Xiang Liguo<br/>
 * <b>Date</b>: 2020/01/15 13:59<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
@RestController
@RequestMapping("/lessons")
// 可以把 RateLimiter 限流放在类上面，也可以放在具体的方法上
//@RateLimiter(name = "lessonController")
public class LessonController {

    public static final Logger LOGGER = LoggerFactory.getLogger(LessonController.class);

    @Autowired
    private LessonService lessonService;

    /**
     * 购买指定 id 的课程
     */
    @Login
    @GetMapping("/buy/{id}")
    // 限流放到方法上
    @RateLimiter(name = "buyById", fallbackMethod = "buyByIdFallback")
    public Lesson buyById(@PathVariable Integer id, HttpServletRequest request) throws InterruptedException {
//        Thread.sleep(1000);
        // 1、根据 id 查询指定的 lesson
        // 2、根据 lesson.id 查询 user_lesson，如果没有
        // 3、如果 user_lesson == null && 用户余额 > lesson.money
        return this.lessonService.buyById(id, request);
    }

    /**
     * 方法名一定要跟 fallbackMethod 中的一样，且返回值和参数列表要跟正常方法的一样
     */
    public Lesson buyByIdFallback(Integer id, HttpServletRequest request, Throwable throwable) {
        LOGGER.info("fallback: ", throwable);
        // 表示从本地缓存中获取
        return new Lesson();
    }
}
