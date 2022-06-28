package com.itheima.study.a39;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Set;

/**
 * @author grzha
 */
@Slf4j
@Configuration
public class A39_1 {

    public static void main(String[] args) {
        /**
         * 这个是Springboot启动的程序，里面做了很多的事
         */
        /*SpringApplication.run(A39_1.class, args);*/
        /**
         * 以下是老师讲的在启动时做的事情，以下5个东西是构造方法里面做的事情
         */
        System.out.println("1.演示获取 Bean Definition 源");
        /**
         * 演示bean的来源，第一个来自于配置类，第二个来自于配置文件
         */
        SpringApplication spring = new SpringApplication(A39_1.class);
        spring.setSources(Set.of("classpath:b01.xml"));
        System.out.println("2.演示推断应用类型");
        System.out.println("3.演示ApplicationContext初始化器");
        System.out.println("4.演示监听器与事件");
        System.out.println("5.演示主类推断");

        ConfigurableApplicationContext context = spring.run(args);
        Arrays.stream(context.getBeanDefinitionNames()).forEach(beanName -> {
            //打印bean的来源
            String description = context.getBeanFactory().getBeanDefinition(beanName).getResourceDescription();
            log.info("======name:{} , 来源:{}", beanName,description);
        });

        context.close();

    }

    public static class Bean1 {

    }

    public static class Bean2 {

    }

    @Bean
    public Bean2 bean2() {
        return new Bean2();
    }


    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }


}
