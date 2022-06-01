package com.ithema.study.a08;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Scope("request")
@Component
@Slf4j
public class BeanForRequest {

    @PreDestroy
    public void destory(){
        log.info("======BeanForRequest被销毁了");
    }


}
