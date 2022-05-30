package com.ithema.study.a05.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class Bean4 {

    public Bean4() {
        log.info("======Bean4 我被spring管理啦");
    }
}
