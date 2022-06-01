package com.ithema.study.a06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class MyConfig1 {

    @Autowired
    public void setApplicationContext(ApplicationContext context){
        log.info("======注入applicationContext:{}",context);
    }

    @PostConstruct
    public void init(){
        log.info("======PostConstruct进行初始化了");
    }

    @Bean
    public BeanFactoryPostProcessor postProcessor1(){
        return configurableListableBeanFactory -> log.info("======执行 postProcessor1");
    }

}
