package com.leekwok.msclass;

import com.leekwok.msclass.feign.GlobalFeignCliengConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients(defaultConfiguration = GlobalFeignCliengConfiguration.class)
@SpringBootApplication
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
        return new RestTemplate();
    }
}
