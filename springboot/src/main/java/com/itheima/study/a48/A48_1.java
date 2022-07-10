package com.itheima.study.a48;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 *  这一讲讲了事件的发布器和监听器，这个是观察者模式，可以把支线任务和主线任务进行解锁
 * @author grzha
 */
@Configuration
@Slf4j
public class A48_1 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(A48_1.class);
        context.getBean(MyService.class).doBusiness();
        context.close();
    }

    /**
     * 自定义事件
     */
    static class MyEvent extends ApplicationEvent{
        /**
         *
         * @param source  事件的源
         */
        public MyEvent(Object source) {
            super(source);
        }
    }

    @Component
    static class MyService{
        /**
         * 这个东西本质就是applicationContext
         */
        @Autowired
        private ApplicationEventPublisher publisher;

        public void doBusiness(){
            log.info("======主线任务");
            publisher.publishEvent(new MyEvent("MyService.doBusiness()"));
            /**
             * 原先支线任务在主逻辑中，如果支线任务老是改，会影响主线任务，这样可以解耦
             */
///            log.info("======发送短信");
///            log.info("======发送邮件");
        }
    }

    /**
     * 发送短信的监听器
     */
    @Component
    @Slf4j
    static class SmsApplicationListener implements ApplicationListener<MyEvent>{

        @Override
        public void onApplicationEvent(MyEvent event) {
            log.info("======发送短信");
        }
    }

    /**
     * 发送邮件的监听器
     */
    @Component
    @Slf4j
    static class EmailApplicationListener implements ApplicationListener<MyEvent>{

        @Override
        public void onApplicationEvent(MyEvent event) {
            log.info("======发送邮件");
        }
    }




}
