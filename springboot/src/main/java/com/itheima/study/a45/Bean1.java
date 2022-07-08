package com.itheima.study.a45;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author grzha
 */
@Component
@Slf4j
public class Bean1 {

    protected Bean2 bean2;

    protected boolean initialized;

    @Autowired
    public void setBean2(Bean2 bean2) {
        log.info("setBean2(Bean2 bean2)");
        this.bean2 = bean2;
    }

    @PostConstruct
    public void init(){
        log.info("init");
        initialized=true;
    }

    public Bean2 getBean2() {
        log.info("getBean2()");
        return bean2;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void m1(){
        System.out.println("m1 成员方法");
    }

    final public void m2(){
        System.out.println("m2 final方法");
    }

    static public void m3(){
        System.out.println("m3 static方法");
    }

    private void m4(){
        System.out.println("m4 private方法");
    }



}
