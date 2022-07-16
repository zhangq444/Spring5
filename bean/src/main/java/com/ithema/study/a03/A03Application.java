package com.ithema.study.a03;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 这一讲讲的是bean的生命周期，从实例化，依赖注入，初始化，销毁方法，然后还可以通过bean的后处理器来增强bean的生命周期
 * @author grzha
 */
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
