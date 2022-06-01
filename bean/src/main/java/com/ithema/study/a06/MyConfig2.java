package com.ithema.study.a06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

@Slf4j
public class MyConfig2 implements ApplicationContextAware, InitializingBean {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("======注入applicationContext:{}",applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("======PostConstruct进行初始化了");
    }

    @Bean
    public BeanFactoryPostProcessor postProcessor1(){
        return configurableListableBeanFactory -> log.info("======执行 postProcessor1");
    }

}
