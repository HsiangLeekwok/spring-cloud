package com.leekwok.msclass.feign;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2020/02/10 18:12<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>: 如果这里加入了 @Configuration 注解的话，这个类需要放到 componentScan 指定的包之外，
 * 否则会出问题，被所有的 Feign 类共享
 */

//@Configuration
public class GlobalFeignClientConfiguration {

    @Bean
    public Logger.Level loggerLevel() {
        return Logger.Level.FULL;
    }
}
