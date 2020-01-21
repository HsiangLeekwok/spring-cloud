package com.leekwok.msclass.configuration;

import com.leekwok.ribbonconfiguration.RibbonConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

/**
 * <b>Author</b>: Xiang Liguo<br/>
 * <b>Date</b>: 2020/01/21 14:41<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: ribbon 的全局配置<br/>
 * <b>Description</b>: ribbon 的全局配置只能用代码方式去配置，不能在配置文件中设置
 */
@Configuration
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
public class GlobalRibbonConfiguration {
}
