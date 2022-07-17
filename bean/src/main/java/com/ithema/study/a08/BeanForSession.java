package com.ithema.study.a08;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Scope("session")
@Component
@Slf4j
@ToString(callSuper = true)
public class BeanForSession {

    @PreDestroy
    public void destory(){
        log.info("======BeanForSession被销毁了");
    }


}
