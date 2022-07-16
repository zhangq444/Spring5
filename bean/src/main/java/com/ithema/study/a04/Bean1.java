package com.ithema.study.a04;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Slf4j
public class Bean1 {

    private Bean2 bean2;

    @Autowired
    public void setBean2(Bean2 bean2) {
        log.info("======Autowired 生效 :{}",bean2);
        this.bean2 = bean2;
    }

    private Bean3 bean3;

    @Resource
    public void setBean3(Bean3 bean3) {
        log.info("======Resource 生效 :{}",bean3);
        this.bean3 = bean3;
    }

    private String home;

    @Autowired
    public void setHome(@Value("${java_home}") String home) {
        log.info("======@Value ,javahome:{}",home);
        this.home = home;
    }

    @Autowired
    private Bean3 bean4;

    @PostConstruct
    public void init(){
        log.info("======PostConstruct");
    }

    @PreDestroy
    public void destroy(){
        log.info("======PreDestroy");
    }

    @Override
    public String toString() {
        return "Bean1{" +
                "bean2=" + bean2 +
                ", bean3=" + bean3 +
                ", home='" + home + '\'' +
                '}';
    }
}
