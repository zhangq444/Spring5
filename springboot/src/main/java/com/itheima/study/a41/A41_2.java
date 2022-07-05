package com.itheima.study.a41;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.env.StandardEnvironment;

/**
 * 这一讲讲了自定义的自动配置类，在spring.factories配置，用org.springframework.boot.autoconfigure.EnableAutoConfiguration作为key
 * 那么springboot就可以自动识别我们写的配置类，自动读取，需要使用@EnableAutoConfiguration注解
 *
 * @author grzha
 */
@Slf4j
public class A41_2 {

    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext();

        context.registerBean(Config.class);
        context.refresh();

        for (String name : context.getBeanDefinitionNames()) {
            log.info("======name:{}", name);
        }


    }

    /**
     * 本项目的配置类
     *
     * @EnableAutoConfiguration是springboot写好的自动配置注解，里面@Import({AutoConfigurationImportSelector.class})会自动读取 只要在我们自己的spring.factories中，用org.springframework.boot.autoconfigure.EnableAutoConfiguration作为key
     * 然后配上自己定义的配置类，springboot就会自己读了
     */
    @Configuration
    @EnableAutoConfiguration
    static class Config {

        /**
         * AnnotationConfigServletWebServerApplicationContext这个容器初始化的时候需要初始化DataSource，而且配了mybatis的jar包
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

    /**
     * 第三方的配置类
     */
    @Configuration
    static class AutoConfiguration1 {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }
    }

    /**
     * 第三方的配置类
     */
    @Configuration
    static class AutoConfiguration2 {
        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }

    static class Bean1 {
    }

    static class Bean2 {

    }


}
