package com.itheima.study.a09;

import com.alibaba.druid.pool.DruidDataSource;
import com.itheima.study.a09.service.MyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

/**
 * 这个是展示Spring的AOP的功能
 */
@SpringBootApplication
@Slf4j
public class A10Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A10Application.class, args);
        /**
         * 看看Springboot启动后容器里面有多少对象
         */
        /*Arrays.stream(context.getBeanDefinitionNames()).forEach(beanName->{
            log.info("======beanName:{}",beanName);
        });*/
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        MyService service = context.getBean(MyService.class);
//        Object myService = context.getBean("myService");//这个和上面一个是同一个对象，类名小写是对象名，hash值是一样的
        /**
         * 启动springboot后，可以看到，MyService类生成的对象是一个spring生成的代理对象，因为增加了切面类，
         * spring会根据切面类和目标类生成代理对象放入容器中
         */
        log.info("======service:{}", service.getClass());
//        log.info("======myService:{}",myService.getClass());

        service.foo();
        context.close();


    }

    /**
     * 加这个是因为加了springboot-jdbc，需要初始化一个DataSource
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
