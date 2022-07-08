package com.itheima.study.a43;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;


/**
 * ComponentScan 要加这个注解，否则不会去扫描，就不会被spring容器管理，
 *  这一讲主要讲了FactoryBean的作用
 * @author grzha
 */
@Slf4j
@ComponentScan
public class A43 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(A43.class);

        /**
         * 往容器里面取的时候，是按照工厂的名字去取，bean1是Bean1FactoryBean的名字，但是取出来的对象是工厂所管理的对象Bean1
         */
        Bean1 bean1 = (Bean1) context.getBean("bean1");
        //设置FactoryBean是单例的
        Bean1 bean2 = (Bean1) context.getBean("bean1");
        Bean1 bean3 = (Bean1) context.getBean("bean1");
        log.info("======bean1:{}",bean1);
        log.info("======bean2:{}",bean2);
        log.info("======bean3:{}",bean3);
        //要想通过类型获得，则FactoryBean中的getObjectType方法一定要写正确
        log.info("======bean4:{}",context.getBean(Bean1.class));
        //要获取FactoryBean这个工厂本身的两种方法,一种按照类型，另一种是工厂的名字前面加一个&
        log.info("======FactoryBean1:{}",context.getBean(Bean1FactoryBean.class));
        log.info("======FactoryBean1:{}",context.getBean("&bean1"));

        context.close();
    }

    /**
     *  如果Bean1PostProcessor不加@Component注解，就是这个bean后处理器不生效时，启动main程序，虽然bean1被创建，但是没有初始化，没有依赖注入等等，spring仅仅是创建了FactoryBean，然后FactoryBean创建了Bean1
     *  如果Bean1PostProcessor加了@Component注解，发现在这个bean后处理器中，before方法没有执行，但是after方法却执行了，说明初始化后的增强逻辑执行了，说明了
     *  工厂bean所管理的产品，在初始化后是受spring管理的，所以是部分受到spring管理
     *
     *
     */




}
