package com.itheima.study.a18;

import com.google.common.collect.Lists;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.*;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 模拟了高级切面转化为低级切面，然后统一转化为环绕通知，然后创建调用链，然后执行调用链
 */
public class A18 {

    /**
     * 切面类
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

        @AfterReturning("execution(* foo())")
        public void afterReturning() {
            System.out.println("afterReturning");
        }

        public void afterThrowing() {
            System.out.println("afterThrowing");
        }

        @Around("execution(* foo())")
        public Object around(ProceedingJoinPoint joinPoint) {
            System.out.println("around");
            Object result = null;
            try {
                result = joinPoint.proceed();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return result;
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


    public static void main(String[] args) throws Throwable {
        //这个视频中没有仔细说，可能是切面类工厂
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
            } else if (method.isAnnotationPresent(AfterReturning.class)) {
                String expression = method.getAnnotation(AfterReturning.class).value();
                //创建切点
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                //创建通知类
                AspectJAfterReturningAdvice advice = new AspectJAfterReturningAdvice(method, pointcut, factory);
                //使用切点和通知创建切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            } else if (method.isAnnotationPresent(Around.class)) {
                String expression = method.getAnnotation(Around.class).value();
                //创建切点
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                //创建通知类
                AspectJAroundAdvice advice = new AspectJAroundAdvice(method, pointcut, factory);
                //使用切点和通知创建切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            }
        }
        list.stream().forEach(System.out::println);

        /**
         * AspectJAfterAdvice           后置通知
         * AspectJAfterReturningAdvice  返回通知
         * AspectJAfterThrowingAdvice   异常通知
         * AspectJAroundAdvice          环绕通知
         *
         * 所有这些通知，加上前置通知，在最后都会转为环绕通知(有一些本身就是环绕通知，就不用转了)，看是否是环绕通知主要看是否实现了MethodInterceptor接口
         */
        //代理工厂，由代理工厂生成代理对象，但是统一转换为环绕通知是在调用代理方法时才转换的
        Target target = new Target();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAdvice(ExposeInvocationInterceptor.INSTANCE);// 准备把 MethodInvocation 放入当前线程（这个是后面用来执行调用链的时候需要用的，这个要放在最外层，应该是要注意添加的顺序的
        proxyFactory.addAdvisors(list);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        //调用代理工厂的方法，主动统一转换为环绕通知
        List<Object> methodInterceptorList = proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(Target.class.getMethod("foo"), Target.class);
        methodInterceptorList.stream().forEach(System.out::println);


        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        //创建并执行调用链(环绕通知+目标)
        //这里不知道为啥,ReflectiveMethodInvocation这个类的对象，视频中老师可以直接通过new创建，但是我看了源码，这个类是被保护的  不能够直接调用构造方法实例化
        /*MethodInvocation methodInvocation=new ReflectiveMethodInvocation(null,target,Target.class.getMethod("foo"),new Object[0],Target.class,methodInterceptorList);
        methodInvocation.proceed();*/
        //所以在这里，我是通过反射，获取了ReflectiveMethodInvocation的构造方法，然后设置可以访问，然后实例化对象的，通过自己处理的，效果试了也可以的
        Constructor<?>[] declaredConstructors = ReflectiveMethodInvocation.class.getDeclaredConstructors();
        Arrays.stream(declaredConstructors).forEach(System.out::println);
        Constructor<ReflectiveMethodInvocation> constructor = (Constructor<ReflectiveMethodInvocation>) declaredConstructors[0];
        constructor.setAccessible(true);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        ReflectiveMethodInvocation foo = constructor.newInstance(null, target, Target.class.getMethod("foo"), new Object[0], Target.class, methodInterceptorList);
        foo.proceed();

        /**
         * 这里下面是我自己加的，这个是参考后面A19写的，就是一开始Spring初始化的时候应该只是生成proxy代理对象，然后放入容器中，
         * 当proxy对象要调用方法时，才会用ReflectiveMethodInvocation，然后创建并执行调用链(环绕通知+目标)，然后执行调用链
         * 然后就可以对目标方法进行增强了，增强的逻辑就在通知之中
         */
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Object proxy = proxyFactory.getProxy();
        MethodInvocation methodInvocation = new ReflectiveMethodInvocation(proxy, target, Target.class.getMethod("foo"), new Object[0], Target.class, methodInterceptorList) {
        };
        methodInvocation.proceed();


    }


}
