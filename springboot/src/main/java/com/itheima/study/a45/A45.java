package com.itheima.study.a45;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.Advised;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Method;

/**
 * 这一讲讲了spring代理机制的一些特性，譬如代理对象不会经过依赖注入和初始化，并且代理对象和目标对象的成员变量不共用数据，还有就是final，static，private方法不能够被增强
 * 注意，SpringBootApplication注解中包含了EnableAutoConfiguration注解，会去读取spring.factories文件，里面配置了A41_2.AutoConfiguration1
 * 里面也有一个Bean1，所以启动这个main方法时需要把a41讲中在spring.factories中的配置注掉，不然启动会报bean重复注册
 *
 * @author grzha
 */
@Slf4j
@SpringBootApplication
public class A45 {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(A45.class, args);

        Bean1 proxy = context.getBean(Bean1.class);
        /**
         *  1.依赖注入和初始化影响的是原始对象
         *  2.代理和目标是两个对象，二者成员变量并不共用数据
         *  注意，SpringBootApplication注解中包含了EnableAutoConfiguration注解，会去读取spring.factories文件，里面配置了A41_2.AutoConfiguration1
         *  里面也有一个Bean1，所以启动这个main方法时需要把a41讲中在spring.factories中的配置注掉，不然启动会报bean重复注册
         *  启动时，执行了依赖注入和初始化方法，打印了相关语句，但是没有执行切面，说明这两个阶段，参与进来的是原始对象，而不是代理对象
         */
        /**
         * 代理对象拿到以后执行下面两个方法，那么这两个方法就会被切面增强
         */
        /*proxy.setBean2(new Bean2());
        proxy.init();*/

        //打印代理对象和目标对象的成员变量
        //代理对象不会经过spring的依赖注入和初始化阶段，所以代理对象的成员变量为初始值或者为空，目标对象就会被赋值
        showProxyAndTarget(proxy);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>");
        //调用代理对象的方法，走了切面的增强逻辑，在代理对象的方法中，会在方法内部去走原始对象的对应的方法，所以是可以获得对应的属性和值的
        System.out.println(proxy.getBean2());
        System.out.println(proxy.isInitialized());

        /**
         * 演示static方法，final方法,private方法均无法增强
         * 代理能够增强的  只有是那些能够被重写的方法,上面三种方法都不能够通过方法重写实现代理
         */
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>");
        proxy.m1();
        proxy.m2();
        proxy.m3();
        Method m4 = Bean1.class.getDeclaredMethod("m4");
        m4.setAccessible(true);
        m4.invoke(proxy);

        context.close();


    }

    /**
     * 在这个案例中使用的是ciglib的代理，分别打印代理对象中的成员变量和目标对象中的成员变量，
     * 看看成员变量数据是否相同
     *
     * @param proxy
     * @throws Exception
     */
    public static void showProxyAndTarget(Bean1 proxy) throws Exception {
        System.out.println(">>>>>>>>>>>>>>代理中的成员变量");
        System.out.println("initialized:" + proxy.initialized);
        System.out.println("bean2:" + proxy.bean2);
        //spring容器中只有代理对象，没有目标对象的
        if (proxy instanceof Advised advised) {
            System.out.println(">>>>>>>>>>>>目标中的成员变量");
            Bean1 target = (Bean1) advised.getTargetSource().getTarget();
            System.out.println("initialized:" + target.initialized);
            System.out.println("bean2:" + target.bean2);
        }
    }

    @Bean
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

}
