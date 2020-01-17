package com.leekwok.msclass.service;

import com.leekwok.msclass.dto.UserDTO;
import com.leekwok.msclass.entity.Lesson;
import com.leekwok.msclass.entity.LessonUser;
import com.leekwok.msclass.repository.LessonRepository;
import com.leekwok.msclass.repository.LessonUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * <b>Author</b>: Xiang Liguo<br/>
 * <b>Date</b>: 2020/01/15 13:54<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonUserRepository lessonUserRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 根据 id 查找用户信息
     */
    public Lesson buyById(Integer id) {
        // 1、根据 id 查询指定的 lesson
        Lesson lesson = this.lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Class is not exists."));
        // 2、根据 lesson.id 查询 user_lesson，如果没有
        LessonUser lessonUser = this.lessonUserRepository.findByLessonId(id);
        if (null != lessonUser) {
            return lesson;
        }
        // 3、如果 user_lesson == null && 用户余额 > lesson.money
        // TODO 登录实现后需重构
        Integer userId = 1;
        UserDTO userDto = restTemplate.getForObject(
                "http://localhost:8081/users/{userId}",
                UserDTO.class,
                userId
        );
        assert userDto != null;
        BigDecimal money = userDto.getMoney().subtract(lesson.getPrice());
        if (money.doubleValue() < 0) {
            throw new IllegalArgumentException("Not enough money to buy lesson.");
        }
        // 购买逻辑
        // 1. 调用用户微服务的扣减金额接口
        // 2. 向 lesson_user 表插入数据
        return lesson;
    }
}
