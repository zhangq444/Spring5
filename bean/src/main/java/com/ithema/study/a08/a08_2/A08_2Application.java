package com.ithema.study.a08.a08_2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 *  这一讲主要讲scope失效的情况
 * @author grzha
 */
@Slf4j
@ComponentScan("com.ithema.study.a08.a08_2")
public class A08_2Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(A08_2Application.class);

        /**
         * E是单例的，F1是多例的，但是由于E的依赖注入只有一次，所以当多次取f1的时候，取到的是同一个对象，这个就是F1的scope失效了
         * 解决的办法是在E中的f1变量上面加上@Lazy注解，这样在注入的时候就会注入F1的代理对象，然后在调用代理对象的方法被调用时，由代理对象
         * 去创建原始的f1对象，这样就是多例的了，注意，要重写toString，原因在第8讲前面讲过，或者加VM虚拟机参数
         *
         * 一共四种解决办法，老师推荐不使用代理的方式解决，使用代理会有性能损耗，所以推荐第3或第4中解决方式
         */
        E e = context.getBean(E.class);
        log.info("======f1:{}",e.getF1());
        log.info("======f1:{}",e.getF1());
        log.info("======f1:{}",e.getF1());
        log.info("======f1:{}",e.getF1().getClass());

        log.info("======f2:{}",e.getF2());
        log.info("======f2:{}",e.getF2());
        log.info("======f2:{}",e.getF2());
        log.info("======f2:{}",e.getF2().getClass());

        log.info("======f3:{}",e.getF3());
        log.info("======f3:{}",e.getF3());
        log.info("======f3:{}",e.getF3());
        log.info("======f3:{}",e.getF3().getClass());

        log.info("======f4:{}",e.getF4());
        log.info("======f4:{}",e.getF4());
        log.info("======f4:{}",e.getF4());
        log.info("======f4:{}",e.getF4().getClass());

        context.close();
    }



}
