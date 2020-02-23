package com.leekwok.msclass;

import com.leekwok.msclass.auth.CheckAuthz;
import com.leekwok.msclass.dto.UserDTO;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.vavr.collection.Seq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test-token-relay")
    public ResponseEntity<UserDTO> testTokenRelay(@RequestHeader("Authorization") String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", token);
       return this.restTemplate.exchange(
                "http://ms-user/users/{id}",
                HttpMethod.GET,
                new HttpEntity<String>(httpHeaders),
                UserDTO.class,
                1
        );
    }

    @GetMapping("/test-token-relay2")
    public UserDTO testTokenRelay2(@RequestHeader("Authorization") String token) {
      return   this.restTemplate.getForObject(
                "http://ms-user/users/{id}",
                UserDTO.class,
                1
        );
    }

    /**
     * 当且仅当用户是 vip 的时候才能访问
     * @param token
     * @return
     */
    @GetMapping("/vip")
    @CheckAuthz(hasRole = "vip")
    public UserDTO vip(@RequestHeader("Authorization") String token) {
        return   this.restTemplate.getForObject(
                "http://ms-user/users/{id}",
                UserDTO.class,
                1
        );
    }
}
