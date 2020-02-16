package com.leekwok.msclass;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.vavr.collection.Seq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <b>Author</b>: Xiang Liguo<br/>
 * <b>Date</b>: 2020/01/20 13:16<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
@RestController
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/test-discovery")
    public List<ServiceInstance> testDiscovery() {
        // 到 consul 上查询指定微服务的所有实例
        return discoveryClient.getInstances("ms-user");
    }

    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    @GetMapping("/rate-limiter-configs")
    public Seq<RateLimiter> test() {
        return this.rateLimiterRegistry.getAllRateLimiters();
    }
}
