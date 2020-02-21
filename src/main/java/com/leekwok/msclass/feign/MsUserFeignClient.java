package com.leekwok.msclass.feign;

import com.leekwok.msclass.dto.UserDTO;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2020/01/21 19:08<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
@FeignClient(name = "ms-user"/*, configuration = MsUserFeignClientConfiguration.class*/)
public interface MsUserFeignClient {

    @RateLimiter(name = "findUserById")
    @GetMapping("/users/{userId}")
    UserDTO findUserById(@PathVariable("userId") Integer userId,
                         @RequestHeader("Authorization") String token);
}
