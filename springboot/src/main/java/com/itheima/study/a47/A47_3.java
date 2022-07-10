package com.itheima.study.a47;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 *  这一讲主要介绍容器中同类型的bean存在多个时，处理的方式
 * @author grzha
 */
@Slf4j
@Configuration
public class A47_3 {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A47_3.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        log.info("=======testPrimary");
        testPrimary(beanFactory);
        log.info("=======testDefault");
        testDefault(beanFactory);

    }

    private static void testDefault(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        DependencyDescriptor descriptor = new DependencyDescriptor(Target2.class.getDeclaredField("service3"), false);
        Class<?> clazz = descriptor.getDependencyType();
        for (String name : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, clazz)) {
            //如果有多个service，并且也没有加@Primary注解，那么最后会根据成员变量的名字进行匹配
            if (name.equals(descriptor.getDependencyName())) {
                Object service = descriptor.resolveCandidate(name, clazz, beanFactory);
                log.info("======最后注入给成员变量的对象service：{}",service);
            }
        }
    }

    private static void testPrimary(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        DependencyDescriptor descriptor = new DependencyDescriptor(Target1.class.getDeclaredField("service"), false);
        Class<?> clazz = descriptor.getDependencyType();
        for (String name : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, clazz)) {
            log.info("======name:{}",name);
            //判断是否加了@Primary注解
            if (beanFactory.getMergedBeanDefinition(name).isPrimary()) {
                Object service = descriptor.resolveCandidate(name, clazz, beanFactory);
                log.info("======最后注入给成员变量的对象service：{}",service);
            }
        }
    }

    static class Target1 {
        @Autowired
        private Service service;
    }

    static class Target2 {
        @Autowired
        private Service service3;
    }

    interface Service {

    }

    @Component("service1")
    static class Service1 implements Service {

    }

    @Component("service2")
    @Primary
    static class Service2 implements Service {

    }

    @Component("service3")
    static class Service3 implements Service {

    }


}
