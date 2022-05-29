package com.ithema.study.a02;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyBeanPostProcessor implements InstantiationAwareBeanPostProcessor, DestructionAwareBeanPostProcessor {


    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        if(StringUtils.equalsIgnoreCase("LifeCycle",beanName)){
            log.info("======销毁方法执行之前");
        }
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName){
        if(StringUtils.equalsIgnoreCase("LifeCycle",beanName)){
            log.info("======构造方法执行之前");
        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName){
        if(StringUtils.equalsIgnoreCase("LifeCycle",beanName)){
            log.info("======构造方法执行之后");
        }
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName){
        if(StringUtils.equalsIgnoreCase("LifeCycle",beanName)){
            log.info("======依赖注入方法执行之前");
        }
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName){
        if(StringUtils.equalsIgnoreCase("LifeCycle",beanName)){
            log.info("======初始化方法执行之前");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName){
        if(StringUtils.equalsIgnoreCase("LifeCycle",beanName)){
            log.info("======初始化方法执行之后");
        }
        return bean;
    }



}
