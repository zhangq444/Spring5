package com.ithema.study.a02;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class A02Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A02Application.class, args);
        log.info("======项目启动了");
        context.close();
    }


}
