package com.ithema.study.a01;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 这一讲主要讲了spring容器的接口，BeanFactory和ApplicationContext，包括了存放单例bean的地方，以及ApplicationContext容器的作用
 * Created by grzha on 2022/5/28.
 */
@SpringBootApplication
@Slf4j
public class A01Application {

    public static void main(String[] args) throws Exception {
        //run方法返回的结果就是spring容器,是一个applicationContext的子接口
        ConfigurableApplicationContext context = SpringApplication.run(A01Application.class, args);
        //在下面一行代码旁边打个断点看ConfigurableApplicationContext ，可以看到有一个beanFactory， 里面有一个singletonObjects，就是放了容器里面的所有初始化好的实例
        //启动时，ConfigurableApplicationContext接口的实现类是AnnotationConfigServletWebServerApplicationContext，然后再这个类的几级父类GenericApplicationContext中有这个beanFactory，
        //这个beanFactory的实现类是DefaultListableBeanFactory，然后在这个DefaultListableBeanFactory的几级父类中，就是DefaultSingletonBeanRegistry这个类中，有这个singletonObjects
        //这个singletonObjects是一个ConcurrentHashMap，里面放了初始化好的实例
        System.out.println(context);

        //这是一个很父级的类，里面包含了singletonObjects，所有单例的实例，是DefaultListableBeanFactory很父级的类
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);

        //这个是一个接口，实现类是DefaultListableBeanFactory，然后他继承了DefaultSingletonBeanRegistry，所以包含singletonObjects属性
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
        map.forEach((k, v) -> {
            System.out.println(k + "--" + v);
        });
        //打印我们自己添加的bean，Component1和Component2
        map.entrySet().stream().filter(entry -> entry.getKey().startsWith("component")).forEach(entry -> {
            log.info("======key:{},value:{}", entry.getKey(), entry.getValue());
        });

        //applicationContext可以根据通配符在查找资源，例如通配符classpath:
        //classpath:这个通配符只能够找类路径的，jar包里面的是不会找的
        Resource[] resources = context.getResources("classpath:application.properties");
        for (Resource resource : resources) {
            log.info("======resource:{}", resource);
        }
        //classpath*: 这个通配符jar包里面的也会找
        Resource[] resources1 = context.getResources("classpath*:META-INF/spring.factories");
        for (Resource resource : resources1) {
            log.info("======resourceNew:{}", resource);
        }

        //容器里面加载了application.properties文件里面的配置信息
        String property = context.getEnvironment().getProperty("app.id");
        System.out.println(property);
        //environment对象里面的数据来源可以是系统的环境变量，也可以是spring的配置文件，例如properties文件
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info("=======environment:{}", environment);

        //最后一个applicationContext可以发布事件，这个在后面的48讲会细讲，就不做记录了
    }

    /**
     * 加了mysql的jar包，所以需要配置DataSource
     *
     * @return
     */
    @Bean
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }


}
