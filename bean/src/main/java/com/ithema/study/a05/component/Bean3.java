package com.ithema.study.a05.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class Bean3 {

    public Bean3() {
        log.info("======Bean3 我被spring管理啦");
    }
}
