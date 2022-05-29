package com.ithema.study.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class TestBeanFactory {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();
        beanFactory.registerBeanDefinition("config",beanDefinition);

        //给beanFactory添加一些常用的后处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().stream().forEach(beanFactoryPostProcessor -> {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });

        beanFactory.getBeansOfType(BeanPostProcessor.class).values().stream().forEach(beanFactory::addBeanPostProcessor);

        String[] names = beanFactory.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }

        beanFactory.preInstantiateSingletons();

        System.out.println(beanFactory.getBean(Bean1.class).getBean2());

    }

    @Configuration
    static class Config{

        @Bean(name = "bean01")
        public Bean1 testBean1(){
            return new Bean1();
        }

        @Bean
        public Bean2 testBean2(){
            return new Bean2();
        }

    }

    static class Bean1{
        @Autowired
        private Bean2 bean2;

        public Bean1() {
            System.out.println("构造了Bean1");
        }

        public Bean2 getBean2() {
            return bean2;
        }

        public void setBean2(Bean2 bean2) {
            this.bean2 = bean2;
        }
    }

    static class Bean2{

        public Bean2() {
            System.out.println("构造了Bean2");
        }

        public void getMsg(){
            System.out.printf("Bean2");
        }
    }





}
