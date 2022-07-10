package com.itheima.study.a48;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 *  对前面进行了改写，使用了@EventListener注解，并且添加了线程池
 * @author grzha
 */
@Configuration
@Slf4j
public class A48_2 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(A48_2.class);
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
        @Autowired
        private ApplicationEventPublisher publisher;

        public void doBusiness(){
            log.info("======主线任务");
            publisher.publishEvent(new MyEvent("MyService.doBusiness()"));
        }
    }

    @Component
    @Slf4j
    static class SmsService{
        @EventListener
        private void listener(MyEvent myEvent){
            log.info("======发送短信");
        }
    }

    @Component
    @Slf4j
    static class EmailService{
        @EventListener
        private void listener(MyEvent myEvent){
            log.info("======发送邮件");
        }
    }

    @Bean
    public ThreadPoolTaskExecutor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        return executor;
    }

    /**
     *  这个是ApplicationEventPublisher底层用的事件发布器对象，我们如果提供了这个，并且设置了线程池，那么事件的处理就不会放在main线程了
     *  就会另外启动一个线程去处理支线任务，就是异步的了
     *  注意，方法名必须是applicationEventMulticaster，这样才能够覆盖原来的变量
     * @param executor
     * @return
     */
    @Bean
    public SimpleApplicationEventMulticaster applicationEventMulticaster(ThreadPoolTaskExecutor executor){
        SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        multicaster.setTaskExecutor(executor);
        return multicaster;
    }


}
