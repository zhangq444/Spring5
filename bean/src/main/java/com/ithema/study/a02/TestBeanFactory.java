package com.ithema.study.a02;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  这一讲主要讲BeanFactory的实现DefaultListableBeanFactory
 * @author grzha
 */
public class TestBeanFactory {

    public static void main(String[] args) {
        //DefaultListableBeanFactory是BeanFactory接口最重要的实现,创建它的对象，他就是spring核心的容器了
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //刚创建DefaultListableBeanFactory，里面是没有bean的，需要给它添加bean的定义，不是直接添加bean，是添加bean的定义，然后DefaultListableBeanFactory就会自己给你创建bean
        //bean的定义有哪些？其实就是描述你这个bean有哪些特征，例如他的类型class，他的域scope（是单例还是多例）,有没有初始化方法，有没有销毁方法等等，bean的定义就是封装
        //了这些信息，然后beanFactory就根据这些描述信息创建bean，并且初始化好
        //创建Config的BeanDefinition，用BeanDefinitionBuilder.genericBeanDefinition方法，获得的就是bean定义对象
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();
        //向beanFactory注册bean定义对象,并起个名字
        beanFactory.registerBeanDefinition("config",beanDefinition);

        //给beanFactory添加一些常用的后处理器(包括bean工厂后处理器和bean的后处理器),单纯的DefaultListableBeanFactory是没有解析@Configuration以及@Bean等注解的能力的
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        // BeanFactory后处理的主要功能，就是补充了一些bean的定义，例如如果不加后处理器，就只有config，加了之后，就会有bean1和bean2的定义,解析@Configuration以及@Bean等注解
        //根据类型BeanFactoryPostProcessor获得所有的bean工厂后处理器
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().stream().forEach(beanFactoryPostProcessor -> {
            //执行每一个bean工厂后处理器，这样就有了扩展功能，能够解析一些注解了
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });

        // 添加bean的后处理器，bean的后处理器是针对bean的生命周期的各个阶段提供扩展，例如bean的创建、依赖注入、初始化等等阶段，例如解析@Autowired @Resource等注解,它和bean工厂的后处理器的作用是不一样的
        //根据类型BeanPostProcessor获得所有bean后处理器，然后把它们添加到beanFactory就可以了，这样加了之后，bean2就会被依赖注入了
        //后bean的处理器的加入顺序决定了后处理器的优先级，哪个先被加入，哪个优先级就高
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().stream().forEach(beanFactory::addBeanPostProcessor);

        //查看beanFactory中有哪些bean的定义
        String[] names = beanFactory.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }

        //下面这个方法是准备好所有的单例对象，这样bean工厂就会创建对象了，否则就是在getBean的时候才会创建对象
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
