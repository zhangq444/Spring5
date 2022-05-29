package com.ithema.study.a01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by grzha on 2022/5/28.
 */
@SpringBootApplication
@Slf4j
public class A01Application {

    public static void main(String[] args) throws Exception{
        ConfigurableApplicationContext context = SpringApplication.run(A01Application.class, args);
        //打个端点看ConfigurableApplicationContext ，可以看到有一个beanFactory， 里面有一个singletonObjects，就是放了容器里面的所有初始化好的实例
        System.out.println(context);

        //这是一个很父级的类，里面包含了singletonObjects，所有单例的实例，是DefaultListableBeanFactory很父级的类
        Field objects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        objects.setAccessible(true);

        //这个是一个接口，实现类是DefaultListableBeanFactory，然后他继承了DefaultSingletonBeanRegistry，所以包含singletonObjects属性
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Map<String,Object> map= (Map<String, Object>) objects.get(beanFactory);
        map.forEach((k,v)->{
            System.out.println(k+"--"+v);
        });

        String property = context.getEnvironment().getProperty("app.id");
        System.out.println(property);



    }

}
