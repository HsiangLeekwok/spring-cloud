package com.leekwok.ribbonconfiguration;

import com.leekwok.msclass.ribbon.CustomRibbonRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <b>Author</b>: Xiang Liguo<br/>
 * <b>Date</b>: 2020/01/21 14:14<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
@Configuration
public class RibbonConfiguration {
    @Bean
    public IRule ribbonRule() {
        // 自定义的规则
        return new CustomRibbonRule();
    }
}
