package com.leekwok.msclass.service;

import com.leekwok.msclass.dto.UserDTO;
import com.leekwok.msclass.dto.UserMoneyDTO;
import com.leekwok.msclass.entity.Lesson;
import com.leekwok.msclass.entity.LessonUser;
import com.leekwok.msclass.feign.MsUserFeignClient;
import com.leekwok.msclass.repository.LessonRepository;
import com.leekwok.msclass.repository.LessonUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * <b>Author</b>: Xiang Liguo<br/>
 * <b>Date</b>: 2020/01/15 13:54<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LessonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LessonService.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonUserRepository lessonUserRepository;

    @Autowired
    private MsUserFeignClient msUserFeignClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Source source;

    /**
     * 根据 id 查找用户信息
     */
    public Lesson buyById(Integer id, HttpServletRequest request) {
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
        Integer userId = (Integer) request.getAttribute("userId");
        // 用 Feign 重构
        UserDTO userDto = msUserFeignClient.findUserById(userId);
        assert userDto != null;
        BigDecimal money = userDto.getMoney().subtract(lesson.getPrice());
        if (money.doubleValue() < 0) {
            throw new IllegalArgumentException("Not enough money to buy lesson.");
        }
        // 购买逻辑
        // 1. 发送消息给用户微服务，让它扣减金额
        this.source.output().send(
                MessageBuilder.withPayload(
                        new UserMoneyDTO(
                                userId,
                                lesson.getPrice(),
                                "购买课程",
                                String.format("%d 购买了 id 为 %d 的课程", userId, id)
                        )
                ).build()
        );
        // 2. 向 lesson_user 表插入数据
        LessonUser lessonUser1 = new LessonUser();
        lessonUser1.setLessonId(id);
        lessonUser1.setUserId(userId);
        this.lessonUserRepository.save(lessonUser1);
        return lesson;
    }
}
