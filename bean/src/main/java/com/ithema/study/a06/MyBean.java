package com.ithema.study.a06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

@Slf4j
public class MyBean implements BeanNameAware, ApplicationContextAware, InitializingBean {

    /**
     * BeanNameAware方法，注入bean的名字
     * @param name
     */
    @Override
    public void setBeanName(String name) {
      log.info("======setBeanName当前bean:{},名字叫:{}",this,name);
    }

    /**
     * ApplicationContextAware的方法，注入ApplicationContext
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("======setApplicationContext当前的容器，类型是:{}",applicationContext);
    }

    /**
     * InitializingBean的方法，afterPropertiesSet初始化方法
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("======当前的bean，afterPropertiesSet初始化操作:{}",this);
    }

    @Autowired
    public void auto(ApplicationContext applicationContext){
        log.info("======Autowired注入ApplicationContext，类型是:{}",applicationContext);
    }

    @PostConstruct
    public void init(){
        log.info("======PostConstruct注解，进行初始化");
    }





}
