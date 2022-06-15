package com.ithema.study.a01;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

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
        //在下面一行代码旁边打个断点看ConfigurableApplicationContext ，可以看到有一个beanFactory， 里面有一个singletonObjects，就是放了容器里面的所有初始化好的实例
        //启动时，ConfigurableApplicationContext接口的实现类是AnnotationConfigServletWebServerApplicationContext，然后再这个类的几级父类GenericApplicationContext中有这个beanFactory，
        //这个beanFactory的实现类是DefaultListableBeanFactory，然后在这个DefaultListableBeanFactory的几级父类中，就是DefaultSingletonBeanRegistry这个类中，有这个singletonObjects
        //这个singletonObjects是一个ConcurrentHashMap，里面放了初始化好的实例
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

        //容器里面加载了application.properties文件里面的配置信息
        String property = context.getEnvironment().getProperty("app.id");
        System.out.println(property);
    }

    /**
     * 加了mysql的jar包，所以需要配置DataSource
     * @return
     */
    @Bean
    public DruidDataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }


}
