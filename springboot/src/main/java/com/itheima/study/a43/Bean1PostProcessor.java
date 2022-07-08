package com.itheima.study.a43;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author grzha
 */
@Component
@Slf4j
public class Bean1PostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equals("bean1")&&bean instanceof Bean1){
            log.info("======before[{}] init",beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equals("bean1")&&bean instanceof Bean1){
            log.info("======after[{}] init",beanName);
        }
        return bean;
    }
}
