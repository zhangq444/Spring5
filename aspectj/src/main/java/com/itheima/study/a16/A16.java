package com.itheima.study.a16;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

public class A16 {

    public static void main(String[] args) throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut=new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* bar())");
        System.out.println(pointcut.matches(T1.class.getMethod("foo"), T1.class));
        System.out.println(pointcut.matches(T1.class.getMethod("bar"), T1.class));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        AspectJExpressionPointcut pointcut1=new AspectJExpressionPointcut();
        pointcut1.setExpression("@annotation(org.springframework.transaction.annotation.Transactional)");
        System.out.println(pointcut1.matches(T1.class.getMethod("foo"), T1.class));
        System.out.println(pointcut1.matches(T1.class.getMethod("bar"), T1.class));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        //模拟Transactional注解解析，因为这个注解可以加在类上和接口上，所以第二个方式不行
        StaticMethodMatcherPointcut pointcut2=new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                //判断方法上面是否有Transactional注解
                MergedAnnotations annotations=MergedAnnotations.from(method);
                if(annotations.isPresent(Transactional.class)){
                    return true;
                }
                //判断类上面是否有Transactional注解
                //这个方法第一个参数是查找目标，从targetClass上去查找注解，第二个参数是查找策略，是不光从本类，并且从他的父类和实现的接口去查找
                annotations=MergedAnnotations.from(targetClass, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY);
                if(annotations.isPresent(Transactional.class)){
                    return true;
                }

                return false;
            }
        };

        System.out.println(pointcut2.matches(T1.class.getMethod("foo"), T1.class));
        System.out.println(pointcut2.matches(T2.class.getMethod("foo"), T2.class));
        System.out.println(pointcut2.matches(T3.class.getMethod("foo"), T3.class));

    }

    static class T1{
        @Transactional
        public void foo(){

        }

        public void bar(){

        }
    }

    @Transactional
    static class T2{
        public void foo(){}
    }

    @Transactional
    interface I3{
        void foo();
    }

    static class T3 implements I3{

        @Override
        public void foo() {

        }
    }



}
