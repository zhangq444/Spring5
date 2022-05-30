package com.ithema.study.a05;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Bean1 {

    public Bean1() {
        log.info("======Bean1 我被spring管理啦");
    }
}
