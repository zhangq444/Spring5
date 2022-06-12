package com.itheima.study.a09.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MyService{

    public void foo(){
        log.info("======foo()");
        bar();
    }

    /**
     * 这个是我自己试验，如果不将自己写的MyAspect交给Spring管理，但是在这个方法上面加上@Transactional注解
     * 那么启动springboot，然后从容器中去拿MyService类的对象，拿到的也是代理对象，所以其实加上@Transactional
     * 注解之后，Spring就会解析这个注解，然后生成代理对象
     */
//    @Transactional
    public void bar(){
        log.info("======bar()");
    }

}
