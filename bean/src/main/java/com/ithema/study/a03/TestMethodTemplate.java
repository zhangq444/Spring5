package com.ithema.study.a03;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *  使用模板模式，模拟bean的后处理器增强bean的生命周期的功能
 * @author grzha
 */
@Slf4j
public class TestMethodTemplate {

    public static void main(String[] args) {
        MyBeanFactory beanFactory = new MyBeanFactory();
        beanFactory.addProcessor(bean -> System.out.println("@Autowired"));
        beanFactory.addProcessor(bean -> System.out.println("@Resource"));
        System.out.println(beanFactory.getBean());


    }

    static class MyBeanFactory{

        private List<BeanPostProcessor> processors=new ArrayList<>();

        public void addProcessor(BeanPostProcessor postProcessor){
            processors.add(postProcessor);
        }

        public Object getBean(){
            Object object = new Object();
            log.info("======构造:{}",object);
            log.info("======依赖注入:{}",object);
            processors.forEach(postProcessor -> {
                postProcessor.inject(object);
            });
            log.info("======初始化:{}",object);
            return object;
        }
    }


    static interface BeanPostProcessor{
        public void inject(Object bean);
    }




}
