package com.ithema.study.a03;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class A03Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A03Application.class, args);
        log.info("======项目启动了");
        context.close();
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
