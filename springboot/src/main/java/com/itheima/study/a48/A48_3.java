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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *  对A48_2进行改写，使用了自定义的注解MyListener
 * @author grzha
 */
@Configuration
@Slf4j
public class A48_3 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(A48_3.class);
        SmsService bean = context.getBean(SmsService.class);
        for (Method method : SmsService.class.getMethods()) {
            //判断方法上面是否加了MyListener注解
            if (method.isAnnotationPresent(MyListener.class)) {
                ApplicationListener listener = new ApplicationListener() {
                    @Override
                    public void onApplicationEvent(ApplicationEvent event) {
                        //获取事件监听器方法所需要的事件类型
                        Class<?> eventType = method.getParameterTypes()[0];
                        //如果真实的事件event的类型能够赋值给eventType，那么就反射调用方法
                        if(eventType.isAssignableFrom(event.getClass())){
                            try {
                                //反射调用标注了@MyListener注解的方法
                                method.invoke(bean,event);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                //将listener加入到spring容器中
                context.addApplicationListener(listener);
            }
        }
        context.getBean(MyService.class).doBusiness();
        context.close();
    }

    static class MyEvent extends ApplicationEvent{
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
        @MyListener
        public void listener(MyEvent myEvent){
            log.info("======发送短信");
        }
    }

    @Component
    @Slf4j
    static class EmailService{
        @MyListener
        public void listener(MyEvent myEvent){
            log.info("======发送邮件");
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface MyListener{
    }



}
