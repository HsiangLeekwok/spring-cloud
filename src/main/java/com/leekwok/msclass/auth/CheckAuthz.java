package com.leekwok.msclass.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2020/02/23 22:31<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
// 元注解：注解在注解类上的注解，总共有 4 个
@Retention(RetentionPolicy.RUNTIME)// 指定注解的保留策略：Runtime表示注解会在字节码中存在，并且可以通过反射获取
public @interface CheckAuthz {

    /**
     * 判断用户的角色
     * @return 返回角色名
     */
    String hasRole();
}
