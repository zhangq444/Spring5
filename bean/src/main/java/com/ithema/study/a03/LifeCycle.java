package com.ithema.study.a03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *  bean的基本的生命周期，包括bean的创建，依赖注入，初始化，销毁
 * @author grzha
 */
@Component
@Slf4j
public class LifeCycle {

    public LifeCycle() {
        log.info("======构造方法");
    }

    @Autowired
    public void getJavaHome(@Value("${java_home}")String javaHome){
        log.info("======依赖注入,javaHome:{}",javaHome);
    }

    @PostConstruct
    public void postConstruct(){
        log.info("======初始化");
    }

    @PreDestroy
    public void preDestroy(){
        log.info("======销毁方法");
    }


}
