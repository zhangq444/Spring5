package com.itheima.study.a49;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * 这一讲讲的是实现自定义的事件发布器
 * 首先，调用doBusiness方法，然后事件发布器发布方法publishEvent，然后就会调用我们定义的事件发布器里面的multicastEvent这个方法，然后在
 * 这个方法里面，会去检查之前收集的所有listener，看是否支持这种类型的事件，如果支持，就调用listener的onApplicationEvent方法，那么事件
 * 监听器里面的逻辑就被执行了
 *
 * @author grzha
 */
@Configuration
@Slf4j
public class A49 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A49.class);
        context.getBean(MyService.class).doBusiness();
        context.close();
    }

    static class MyEvent extends ApplicationEvent {
        public MyEvent(Object source) {
            super(source);
        }
    }

    @Component
    static class MyService {
        @Autowired
        private ApplicationEventPublisher publisher;

        public void doBusiness() {
            log.info("======主线任务");
            publisher.publishEvent(new MyEvent("MyService.doBusiness()"));
        }
    }

    @Component
    @Slf4j
    static class SmsApplicationListener implements ApplicationListener<MyEvent> {

        @Override
        public void onApplicationEvent(MyEvent event) {
            log.info("======发送短信");
        }
    }

    @Component
    @Slf4j
    static class EmailApplicationListener implements ApplicationListener<MyEvent> {

        @Override
        public void onApplicationEvent(MyEvent event) {
            log.info("======发送邮件");
        }
    }

    @Bean
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        return executor;
    }

    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster(ConfigurableApplicationContext context, ThreadPoolTaskExecutor executor) {
        return new AbstractApplicationEventMulticaster() {
            List<GenericApplicationListener> listenerList = new ArrayList<>();

            /**
             * 这个方法是收集事件监听器的
             * @param listenerBeanName
             */
            @Override
            public void addApplicationListenerBean(String listenerBeanName) {
                ApplicationListener listener = context.getBean(listenerBeanName, ApplicationListener.class);
                log.info("======listener:{}", listener);
                //获取该监听器支持的事件类型
                //通过listener的class，获取它实现的接口，再获取接口里面的泛型参数，就获得了事件的类型
                ResolvableType type = ResolvableType.forClass(listener.getClass()).getInterfaces()[0].getGeneric(0);
                //将原始的listener封装为支持事件类型检查的listener，就是GenericApplicationListener这个类型
                GenericApplicationListener genericApplicationListener = new GenericApplicationListener() {
                    /**
                     * 是否支持某事件类型
                     * @param eventType
                     * @return
                     */
                    @Override
                    public boolean supportsEventType(ResolvableType eventType) {
                        //判断真实的事件类型eventType能否赋值给支持的事件类型type
                        return type.isAssignableFrom(eventType);
                    }

                    /**
                     * 在这个方法中调用原始listener的onApplicationEvent方法
                     * 要上一个方法检查为true时才会调用
                     * @param event
                     */
                    @Override
                    public void onApplicationEvent(ApplicationEvent event) {
                        //通过线程池，开启线程发布，这样就是异步的了
                        executor.submit(() -> {
                            listener.onApplicationEvent(event);
                        });
                    }
                };
                listenerList.add(genericApplicationListener);
            }

            /**
             * 这个方法是用来发布事件的
             * @param event
             * @param eventType
             */
            @Override
            public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {
                for (GenericApplicationListener listener : listenerList) {
                    //检查真实事件类型是否支持
                    if (listener.supportsEventType(ResolvableType.forClass(event.getClass()))) {
                        listener.onApplicationEvent(event);
                    }
                }
            }
        };
    }

    abstract static class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster {

        @Override
        public void addApplicationListener(ApplicationListener<?> listener) {

        }

        @Override
        public void addApplicationListenerBean(String listenerBeanName) {

        }

        @Override
        public void removeApplicationListener(ApplicationListener<?> listener) {

        }

        @Override
        public void removeApplicationListenerBean(String listenerBeanName) {

        }

        @Override
        public void removeApplicationListeners(Predicate<ApplicationListener<?>> predicate) {

        }

        @Override
        public void removeApplicationListenerBeans(Predicate<String> predicate) {

        }

        @Override
        public void removeAllListeners() {

        }

        @Override
        public void multicastEvent(ApplicationEvent event) {

        }

        @Override
        public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {

        }
    }


}
