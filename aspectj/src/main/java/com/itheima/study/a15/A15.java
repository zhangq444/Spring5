package com.itheima.study.a15;


import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class A15 {

/*    @Aspect
    static class MyAspect{

        @Before("execution(* foo())")
        public void before(){
            System.out.println("前置通知");
        }

        @After("execution(* foo())")
        public void after(){
            System.out.println("后置通知");
        }

    }*/



    public static void main(String[] args) {
        /**
         * Spring的切面，有两种，一种是Aspect类，这个里面包含了两个东西，一个是通知，就是上面中的方法，例如before和after，这个是通知，
         * 另外一个是切点，切点指的是要执行这些通知的地方，就是注解里面的execution里面的内容，这两个通知和切点一起就组成了切面，然后一个
         * Aspect类中可以包括好多个切点加通知的组合，这整个就是一个切面类，这是Spring中的第一种切面
         * Spring中的另外一个切面是adviser，这个是一个切点加一个通知的组合，是spring底层实现切面的粒度，Aspect类中的多组切点加通知的组合
         * 都会转化为这种细粒度的adviser。
         */
        /**
         * 准备好切点
         */
        AspectJExpressionPointcut pointcut=new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* foo())");
        /**
         * 准备好通知
         */
        //这里不要和cglib的MethodInterceptor搞混，和前面的增强的接口不同，包名不同
        MethodInterceptor advice=invocation -> {
            System.out.println("======before");
            Object result = invocation.proceed();//调用目标
            System.out.println("======after");
            return result;
        };
        /**
         * 准备切面
         */
        DefaultPointcutAdvisor advisor=new DefaultPointcutAdvisor(pointcut,advice);
        /**
         * 创建代理
         *      在ProxyFactory的父类ProxyConfig中有一个属性proxyTargetClass
         *      a.proxyTargetClass=false,目标实现了接口，用jdk
         *      b.proxyTargetClass=false,目标没有实现接口，用cglib
         *      c.proxyTargetClass=true,总是用cglib
         */
        Target1 target1=new Target1();
        //Spring提供了一个代理工厂，内部他会根据情况选择jdk代理或者cglib代理，底层还是在前面讲的两个代理实现的
        ProxyFactory factory=new ProxyFactory();
        factory.setTarget(target1);
        factory.addAdvisor(advisor);
        //下面这个设置可有可无，如果没有，就认为没有实现接口，用的是cglib代理，如果有，就认为实现接口，就会用jdk代理
        factory.setInterfaces(target1.getClass().getInterfaces());
//        factory.setProxyTargetClass(true);
        I1 proxy = (I1) factory.getProxy();
        //如果要看选择的是哪种代理，可以打印proxy的类型，可以看出是jdk代理还是cglib代理
        System.out.println("======proxy:"+proxy.getClass());
        proxy.foo();
        proxy.bar();


    }

    interface I1{
        void foo();
        void bar();
    }

    static class Target1 implements I1{
        @Override
        public void foo() {
            System.out.println("======target1 foo");
        }

        @Override
        public void bar() {
            System.out.println("======target1 foo");
        }
    }

    static class Target2{
        public void foo() {
            System.out.println("======target2 foo");
        }

        public void bar() {
            System.out.println("======target2 foo");
        }
    }









}
