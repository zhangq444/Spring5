package com.itheima.study.a19;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 模拟动态通知调用过程
 */
public class A19 {

    @Aspect
    static class MyAspect {

        @Before("execution(* foo(..))")  // 静态通知调用，不带参数绑定，执行时不需要切点
        public void before1() {
            System.out.println("before1");
        }

        @Before("execution(* foo(..)) && args(x)")  // 动态通知调用，需要参数绑定，执行时需要切点
        public void before2(int x) {
            System.out.printf("before2(%d)%n", x);
        }
    }

    static class Target {
        public void foo(int x) {
            System.out.printf("target foo(%d)%n", x);
        }
    }

    @Configuration
    static class Config {
        /**
         * 这是一个bean后处理器，是用来解析@Aspect注解，并将高级切面类转化为低级切面类，就是将@Aspect注解修饰的类转化为一个个Advisor对象
         * 然后里面提供了一个A15中讲的ProxyFactory，生成代理对象
         */
        @Bean
        AnnotationAwareAspectJAutoProxyCreator proxyCreator() {
            return new AnnotationAwareAspectJAutoProxyCreator();
        }

        @Bean
        public MyAspect myAspect() {
            return new MyAspect();
        }
    }


    public static void main(String[] args) throws Throwable {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.registerBean(Config.class);
        context.refresh();

        AnnotationAwareAspectJAutoProxyCreator proxyCreator = context.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
        /**
         * 视频中老师直接调用findEligibleAdvisors方法，但是我看了源码，这个是AnnotationAwareAspectJAutoProxyCreator这个类的几级父类中的
         * 受保护的方法，不能够直接调用，所以我还是通过反射调用的
         */
//        proxyCreator.findEligibleAdvisors()

        Arrays.stream(AbstractAdvisorAutoProxyCreator.class.getDeclaredMethods()).forEach(System.out::println);
        Method findEligibleAdvisors = AbstractAdvisorAutoProxyCreator.class.getDeclaredMethod("findEligibleAdvisors", Class.class, String.class);
        findEligibleAdvisors.setAccessible(true);
        /**
         * 通过反射调用findEligibleAdvisors方法，获得所有低级环绕通知的list
         */
        List<Advisor> list = (List<Advisor>) findEligibleAdvisors.invoke(proxyCreator, Target.class, "target");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        list.stream().forEach(System.out::println);

        /**
         * 创建代理对象
         */
        Target target = new Target();
        ProxyFactory factory = new ProxyFactory();
        factory.setTarget(target);
        factory.addAdvisors(list);
        Object proxy = factory.getProxy();//但是没有用代理对象去调用方法
        System.out.println("-------------------------------");
        /**
         * 这里使用代理对象直接调用方法去调用通知，这个时候已经貌似可以增强了，但是老师说调用链是在代理方法调用的时候才会生成的（前面的视频说的），所以不要在意这一小段代码，这个老师也没有细说
         */
        Target proxyTemp = (Target) proxy;
        proxyTemp.foo(200);
        System.out.println("-------------------------------");

        /**
         * 通过代理工厂，将通知全部转换为环绕通知
         */
        List<Object> interceptorList = factory.getInterceptorsAndDynamicInterceptionAdvice(Target.class.getMethod("foo", int.class), Target.class);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        interceptorList.stream().forEach(intercepter -> {
            System.out.println(intercepter);
            showDetail(intercepter);
        });

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        /**
         * 通过调用链对象，调用环绕通知
         */
        /**
         * 这里老师做了解释，ReflectiveMethodInvocation这个类的构造方法是受保护的，不能够直接调，所以老师创建了这个类的一个子类对象，子类对象可以调父类受保护的构造方法，
         * 所以在这一行代码的最后一行会多加一个{}。之前学的时候我这个地方是自己用反射拿到构造方法，然后通过反射创建对象的
         */
        MethodInvocation methodInvocation = new ReflectiveMethodInvocation(proxy, target, Target.class.getMethod("foo", int.class), new Object[]{100}, Target.class, interceptorList) {
        };
        methodInvocation.proceed();

        /**
         * 以下这些是我的猜想，可以这么理解加以记忆，可能Spring是将目标类和低级切面通知的list一起生成了代理类，包括了target和List<Advisor> list(这时还并不全是环绕通知)，然后生成了proxy
         * 然后再proxy代理对象调用foo方法时，这个foo方法内部，可以生成interceptorList，就是将所有通知转化为环绕通知，所需的参数就是getInterceptorsAndDynamicInterceptionAdvice这个方法参数，
         * 然后再生成调用链，就是ReflectiveMethodInvocation对象，所需的参数也都有，然后执行调用链，其中就会执行通知和目标方法，然后的到结果，再将结果返回，
         * 那么从外部开来，这个方法就被切面类增强了。
         * 注意：这个只是我的猜想，是这么理解的，视频中老师没有这么说，就说了一句“代理工厂，由代理工厂生成代理对象，但是统一转换为环绕通知是在调用代理方法时才转换的”，
         * 这句话在A18中有，可以看一下视频的第18讲
         *
         *
         *
         */

    }

    /**
     * 由于动态通知形成的对象是InterceptorAndDynamicMethodMatcher对象，这个类不是public的类，无法看到里面的属性，
     * 所以老师写了一段代码，通过反射加载这个类，然后展示里面的成员变量，看是不是环绕通知
     *
     * @param o
     */
    public static void showDetail(Object o) {
        try {
            Class<?> clazz = Class.forName("org.springframework.aop.framework.InterceptorAndDynamicMethodMatcher");
            if (clazz.isInstance(o)) {
                Field methodMatcher = clazz.getDeclaredField("methodMatcher");
                methodMatcher.setAccessible(true);
                Field interceptor = clazz.getDeclaredField("interceptor");
                interceptor.setAccessible(true);
                System.out.println(methodMatcher.get(o));
                System.out.println(interceptor.get(o));
            }
        } catch (Exception e) {

        }
    }


}
