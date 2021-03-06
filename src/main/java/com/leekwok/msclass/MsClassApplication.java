package com.leekwok.msclass;

import com.leekwok.msclass.resttemplate.TokenRelayRequestInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@EnableFeignClients//(defaultConfiguration = GlobalFeignCliengConfiguration.class)
@SpringBootApplication
@EnableBinding({Source.class/*, MySource.class*/})
public class MsClassApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsClassApplication.class, args);
    }

    /**
     * 轻量级的 HttpClient
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(
                Collections.singletonList(new TokenRelayRequestInterceptor())
        );
        return restTemplate;
    }
}
