package com.ithema.study.a07;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 *  这一讲主要讲初始化和销毁
 * @author grzha
 */
@SpringBootApplication
public class A07Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A07Application.class, args);
        context.close();
    }

    @Bean(initMethod = "init3")
    public Bean1 bean1(){
        return new Bean1();
    }

    @Bean(destroyMethod = "destory3")
    public Bean2 bean2(){
        return new Bean2();
    }

    /**
     * 这个没有关系，主要是jar包里面必须配置这个
     * @return
     */
    @Bean(initMethod = "init")
    public DruidDataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

}
