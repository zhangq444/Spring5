package com.itheima.study.a09.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Component//切面类也需要被spring管理，不然aop就不会生效
@Slf4j
@Aspect
public class MyAspect {

    @Before("execution(* com.itheima.study.a09.service.MyService.*())")
    public void before(){
        log.info("======before");
    }



}


