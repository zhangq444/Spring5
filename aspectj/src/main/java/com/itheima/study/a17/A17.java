package com.itheima.study.a17;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.Order;

import java.util.Arrays;

@Slf4j
public class A17 {

    public static void main(String[] args) {
        GenericApplicationContext context=new GenericApplicationContext();
        context.registerBean("aspect1",Aspect1.class);
        context.registerBean("config",Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
        /**
         * 这是一个bean后处理器，是用来解析@Aspect注解，并将高级切面类转化为低级切面类，就是将@Aspect注解修饰的类转化为一个个Advisor对象
         * 然后里面提供了一个A15中讲的ProxyFactory，生成代理对象
         */
        context.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);
        context.registerBean(Target1.class);

        context.refresh();
        Arrays.stream(context.getBeanDefinitionNames()).forEach(beanName->{log.info("======beanName:{}",beanName);});
        Target1 bean = context.getBean(Target1.class);
        log.info("======bean:{}",bean.getClass().getName());
        bean.foo();

    }

    static class Target1{
        public void foo(){
            System.out.println("======Target1 foo");
        }
    }

    static class Target2{
        public void bar(){
            System.out.println("======Target2 bar");
        }
    }

    @Aspect
    @Order(value = 1)
    static class Aspect1{

        @Before("execution(* foo())")
        public void before(){
            System.out.println("======Aspect1 before");
        }

        @After("execution(* foo())")
        public void after(){
            System.out.println("======Aspect1 after");
        }
    }

    @Configuration
    static class Config{
        @Bean
        public Advisor advisor3(MethodInterceptor advice3){
            AspectJExpressionPointcut pointcut=new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice3);
            advisor.setOrder(2);
            return advisor;
        }

        @Bean
        public MethodInterceptor advice3(){
            return new MethodInterceptor() {
                @Override
                public Object invoke(MethodInvocation invocation) throws Throwable {
                    System.out.println("======advice3 before...");
                    Object result = invocation.proceed();
                    System.out.println("======advice3 after...");
                    return result;
                }
            };
        }

    }





}
