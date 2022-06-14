package com.itheima.study.a17;

import com.google.common.collect.Lists;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectInstanceFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 这个是模拟了高级切面类转化为低级切面类的过程
 */
public class A17_2 {

    /**
     * 高级切面类
     */
    static class Aspect {
        @Before("execution(* foo())")
        public void before1() {
            System.out.println("before1");
        }

        @Before("execution(* foo())")
        public void before2() {
            System.out.println("before2");
        }

        public void after() {
            System.out.println("after");
        }

        public void afterReturning() {
            System.out.println("afterReturning");
        }

        public void afterThrowing() {
            System.out.println("afterThrowing");
        }

        public Object around(ProceedingJoinPoint joinPoint) {
            System.out.println("around");
            return null;
        }

    }

    /**
     * 目标类
     */
    static class Target {
        public void foo() {
            System.out.println("target foo...");
        }
    }


    public static void main(String[] args) {
        //这个视频中没有仔细说，可能是切面类工厂(个人理解应该是用来生成低级切面类的工厂)
        AspectInstanceFactory factory = new SingletonAspectInstanceFactory(new Aspect());

        //高级切面类转换为低级切面类的方式
        List<Advisor> list = Lists.newArrayList();
        for (Method method : Aspect.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                //获取有Before注解标注的方法中，注解的切点表达式
                String expression = method.getAnnotation(Before.class).value();
                //创建切点
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                //创建通知类,举例中是使用前置通知类,就是Before注解对应的底层实现的通知类
                AspectJMethodBeforeAdvice advice = new AspectJMethodBeforeAdvice(method, pointcut, factory);
                //使用切点和通知创建切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            }
        }
        list.stream().forEach(System.out::println);


    }


}
