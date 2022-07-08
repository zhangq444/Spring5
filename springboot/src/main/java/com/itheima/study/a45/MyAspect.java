package com.itheima.study.a45;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author grzha
 */
@Aspect
@Component
@Slf4j
public class MyAspect {


    @Before("execution(* com.itheima.study.a45.Bean1.*(..))")
    public void before(){
        log.info("before()");
    }


}
