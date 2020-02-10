package com.leekwok.msclass.feign;

import com.leekwok.msclass.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2020/01/21 19:08<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
@FeignClient(name = "ms-user", configuration = MsUserFeignCliengConfiguration.class)
public interface MsUserFeignClient {

    @GetMapping("/users/{userId}")
    UserDTO findUserById(@PathVariable("userId") Integer userId);
}
