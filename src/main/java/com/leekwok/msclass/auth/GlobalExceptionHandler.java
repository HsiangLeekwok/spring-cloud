package com.leekwok.msclass.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <b>Author</b>: Xiang Liguo<br/>
 * <b>Date</b>: 2020/02/21 15:30<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Response> result(SecurityException exception) {
        Response response = new Response(exception.getMessage(), 401);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}

class Response {
    private String message;
    private Integer code;

    public Response() {
    }

    public Response(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
