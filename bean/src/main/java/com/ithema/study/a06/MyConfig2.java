package com.ithema.study.a06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

/**
 *  这个是针对MyConfig1的对比，虽然里面也有BeanFactoryPostProcessor，但是注入和初始化都不会失效，因为使用的是Aware和InitializingBean接口
 * @author grzha
 */
@Slf4j
public class MyConfig2 implements ApplicationContextAware, InitializingBean {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("======注入applicationContext:{}",applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("======afterPropertiesSet进行初始化了");
    }

    @Bean
    public BeanFactoryPostProcessor postProcessor1(){
        return configurableListableBeanFactory -> log.info("======执行 postProcessor1");
    }

}
