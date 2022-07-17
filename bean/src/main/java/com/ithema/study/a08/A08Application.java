package com.ithema.study.a08;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 *  这一讲主要是将spring中的scope
 * @author grzha
 */
@SpringBootApplication
public class A08Application {

    /**
     *  目前spring中存在5中scope，分别为singleton，prototype，request，session，application
     * @param args
     */

    public static void main(String[] args) {
        SpringApplication.run(A08Application.class, args);


    }

    @Bean(initMethod = "init")
    public DruidDataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

}
