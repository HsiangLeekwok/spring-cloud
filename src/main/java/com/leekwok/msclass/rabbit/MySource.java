package com.leekwok.msclass.rabbit;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2020/02/12 01:19<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public interface MySource {

    String MY_OUTPUT = "myOutput";

    @Output(MY_OUTPUT)
    MessageChannel output();
}
