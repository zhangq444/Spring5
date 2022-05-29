package com.ithema.study;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
        System.out.println(context);

        Field objects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        objects.setAccessible(true);

        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Map<String,Object> map= (Map<String, Object>) objects.get(beanFactory);
        map.forEach((k,v)->{
            System.out.println(k+"--"+v);
        });

        String property = context.getEnvironment().getProperty("app.id");
        System.out.println(property);



    }

}
