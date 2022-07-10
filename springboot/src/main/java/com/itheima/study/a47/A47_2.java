package com.itheima.study.a47;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 这一讲讲的是如何通过@Autowired注入一些集合类型,例如注入service层，dao层
 * 里面根据autowired标注的变量，获取对应的类型，再获取要注入的变量类型，然后从容器中获取对应的bean，在进行类型转换和封装，最后变为可以赋值给成员变量的对象
 *  这里介绍的是A47_1中的doResolveDependency方法的内部逻辑
 * @author grzha
 */
@Slf4j
@Configuration
public class A47_2 {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A47_2.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        log.info("=================数组类型");
        testArray(beanFactory);
        log.info("=================List类型");
        testList(beanFactory);
        log.info("=================applicationContext类型");
        testApplicationContext(beanFactory);
        log.info("=================Generic");
        testGeneric(beanFactory);
        log.info("=================Qualifier");
        testQualifier(beanFactory);

    }

    private static void testQualifier(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException, IllegalAccessException {
        DependencyDescriptor descriptor = new DependencyDescriptor(Target.class.getDeclaredField("service"), true);
        Class<?> clazz = descriptor.getDependencyType();
        ContextAnnotationAutowireCandidateResolver resolver=new ContextAnnotationAutowireCandidateResolver();
        resolver.setBeanFactory(beanFactory);
        for (String name : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, clazz)) {
///            log.info("======name:{}",name);
            BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(name);
            //比较beanDefinition和descriptor的Qualifier是否一致,和泛型比价是同一个方法
            if (resolver.isAutowireCandidate(new BeanDefinitionHolder(beanDefinition,name),descriptor)) {
                log.info("======一致的name:{}",name);
                Object service = descriptor.resolveCandidate(name, clazz, beanFactory);
                log.info("======service:{},class:{}",service,service.getClass());
            }
        }

    }

    private static void testGeneric(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException, IllegalAccessException {
        DependencyDescriptor descriptor = new DependencyDescriptor(Target.class.getDeclaredField("dao"), true);
        Class<?> clazz = descriptor.getDependencyType();
        ContextAnnotationAutowireCandidateResolver resolver=new ContextAnnotationAutowireCandidateResolver();
        resolver.setBeanFactory(beanFactory);
        for (String name : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, clazz)) {
            //根据类型找，会有两个符合条件的dao，因为没有考虑里面的泛型
///            log.info("======name:{}",name);
            BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(name);
            // 比较beanDefinition和descriptor的泛型信息是否一致
            if (resolver.isAutowireCandidate(new BeanDefinitionHolder(beanDefinition,name),descriptor)) {
                log.info("======一致的name:{}",name);
                Object dao = descriptor.resolveCandidate(name, clazz, beanFactory);
                log.info("======dao:{},class:{}",dao,dao.getClass());
            }
        }
    }

    private static void testApplicationContext(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException, IllegalAccessException {
        DependencyDescriptor descriptor = new DependencyDescriptor(Target.class.getDeclaredField("applicationContext"), true);
        //applicationContext对象是放在resolvableDependencies这个成员变量中的，不是放在DefaultSingletonBeanRegistry这个类的singletonObjects中的
        Field resolvableDependencies = DefaultListableBeanFactory.class.getDeclaredField("resolvableDependencies");
        resolvableDependencies.setAccessible(true);
        Map<Class<?>, Object> dependencies = (Map<Class<?>, Object>) resolvableDependencies.get(beanFactory);
        dependencies.forEach((key,value)->{
            log.info("======key:{},value:{}",key,value);
        });
        for (Map.Entry<Class<?>, Object> entry : dependencies.entrySet()) {
            //判断要注入的成员变量的类型能否赋值给entry的key的类型
            if (entry.getKey().isAssignableFrom(descriptor.getDependencyType())) {
                log.info("======要注入给成员变量的对象为：{}",entry.getValue());
                break;
            }
        }
    }

    private static void testList(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        DependencyDescriptor descriptor = new DependencyDescriptor(Target.class.getDeclaredField("serviceList"), true);
        if (descriptor.getDependencyType() == List.class) {
            //获取List中的泛型
            Class<?> clazz = descriptor.getResolvableType().getGeneric().resolve();
            log.info("======clazz:{}", clazz);
            String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, clazz);
            List<Object> list = new ArrayList<>();
            for (String name : names) {
                Object bean = descriptor.resolveCandidate(name, clazz, beanFactory);
                list.add(bean);
            }
            log.info("======list:{}", list);
        }
    }

    private static void testArray(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        DependencyDescriptor descriptor = new DependencyDescriptor(Target.class.getDeclaredField("serviceArray"), true);
        //首先判断是否是数组类型
        if (descriptor.getDependencyType().isArray()) {
            //拿到数组的元素类型
            Class<?> clazz = descriptor.getDependencyType().componentType();
            log.info("======clazz:{}", clazz);
            //工具类，根据类型，到bean工厂的容器以及祖先的容器中找对应的bean的名字
            String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, clazz);
            List<Object> beans = new ArrayList<>();
            for (String name : names) {
                log.info("======name:{}", name);
                //从beanFactory通过名字获取相应的bean，这个是用现成的方法
                Object bean = descriptor.resolveCandidate(name, clazz, beanFactory);
                beans.add(bean);
            }
            //通过beanFactory的类型转换器，将值（第一个参数），转换为对应的类型（第二个参数）
            Object array = beanFactory.getTypeConverter().convertIfNecessary(beans, descriptor.getDependencyType());
            //这个打印不好看  可打一个断点看看array
            log.info("======array:{}", JSON.toJSONString(array));

        }
    }

    static class Target {
        @Autowired
        private Service[] serviceArray;
        @Autowired
        private List<Service> serviceList;
        @Autowired
        private ConfigurableApplicationContext applicationContext;
        @Autowired
        private Dao<Teacher> dao;
        @Autowired
        @Qualifier("service2")
        private Service service;
    }

    interface Service {

    }

    @Component("service1")
    static class Service1 implements Service {

    }

    @Component("service2")
    static class Service2 implements Service {

    }

    @Component("service3")
    static class Service3 implements Service {

    }

    interface Dao<T> {

    }

    static class Student {

    }

    static class Teacher {

    }

    @Component("dao1")
    static class Dao1 implements Dao<Student> {

    }

    @Component("dao2")
    static class Dao2 implements Dao<Teacher> {

    }

}
