package com.leekwok.msclass.controller;

import com.leekwok.msclass.entity.Lesson;
import com.leekwok.msclass.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>Author</b>: Xiang Liguo<br/>
 * <b>Date</b>: 2020/01/15 13:59<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
@RestController
@RequestMapping("/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    /**
     * 购买指定 id 的课程
     */
    @GetMapping("/buy/{id}")
    public Lesson buyById(@PathVariable Integer id) {
        // 1、根据 id 查询指定的 lesson
        // 2、根据 lesson.id 查询 user_lesson，如果没有
        // 3、如果 user_lesson == null && 用户余额 > lesson.money
        return this.lessonService.buyById(id);
    }
}
