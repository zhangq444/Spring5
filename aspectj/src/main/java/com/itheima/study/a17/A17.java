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

/**
 * 这个是展示了高级切面和低级切面和目标类一起被Spring生成代理对象，同时被高级切面和低级切面增强
 */
@Slf4j
public class A17 {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("aspect1", Aspect1.class);
        context.registerBean("config", Config.class);
        //这个是用来解析@Configuration的bean后置处理器
        context.registerBean(ConfigurationClassPostProcessor.class);
        /**
         * 这是一个bean后处理器，是用来解析@Aspect注解，并将高级切面类转化为低级切面类，就是将@Aspect注解修饰的类转化为一个个Advisor对象
         * 然后里面提供了一个A15中讲的ProxyFactory，生成代理对象
         */
        context.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);
        context.registerBean(Target1.class);

        context.refresh();
        Arrays.stream(context.getBeanDefinitionNames()).forEach(beanName -> {
            log.info("======beanName:{}", beanName);
        });
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        /**
         * 获取目标类的对象，这个时候Spring已经生成了代理对象
         */
        Target1 bean = context.getBean(Target1.class);
        log.info("======bean:{}", bean.getClass().getName());
        /**
         * 调用代理对象的方法，已经被高级切面以及低级切面增强了
         */
        bean.foo();

    }

    /**
     * 目标类
     */
    static class Target1 {
        public void foo() {
            System.out.println("======Target1 foo");
        }
    }

    /**
     * 高级切面类，里面有2个切点和通知的组合
     */
    @Aspect
    @Order(value = 1)//order数字越小，级别越高，默认的级别是最低的，好像用的是int的最大值
    static class Aspect1 {

        @Before("execution(* foo())")
        public void before() {
            System.out.println("======Aspect1 before");
        }

        @After("execution(* foo())")
        public void after() {
            System.out.println("======Aspect1 after");
        }
    }

    @Configuration
    static class Config {
        /**
         * 配置一个低级的切面，自己用切点加上通知生成一个低级的漆面，切面表达式和高级切面是一样的
         *
         * @param advice3
         * @return
         */
        @Bean
        public Advisor advisor3(MethodInterceptor advice3) {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice3);
            advisor.setOrder(2);//设置切面的优先级
            return advisor;
        }

        /**
         * 配置一个通知，模拟环绕通知
         *
         * @return
         */
        @Bean
        public MethodInterceptor advice3() {
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
