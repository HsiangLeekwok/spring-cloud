package com.leekwok.msclass.configuration;

import com.leekwok.ribbonconfiguration.RibbonConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

/**
 * <b>Author</b>: Xiang Liguo<br/>
 * <b>Date</b>: 2020/01/21 14:12<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 细粒度配置类<br/>
 * <b>Description</b>: 配置类放在这里是为了避免 SpringBoot启动类的 @ComponentScan 扫描注解时将其设为全局配置影响所有的 ribbonClient
 */
//@Configuration
//@RibbonClient(name = "ms-user", configuration = RibbonConfiguration.class)
public class MsUserRibbonConfiguration {
}
