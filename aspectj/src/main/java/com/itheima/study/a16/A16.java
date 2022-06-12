package com.itheima.study.a16;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * 这个是讲解，切点表达式和目标类中的方法或者目标类是如何匹配的
 */
public class A16 {

    public static void main(String[] args) throws NoSuchMethodException {
        /**
         * 第一部分是测试T1类中的两个方法，是否满足切点表达式(使用的是execution的表达式)，使用的是pointcut.matches()这个方法
         */
        //准备好切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* bar())");
        //打印看T1类中的两个方法是否满足切点表达式
        System.out.println(pointcut.matches(T1.class.getMethod("foo"), T1.class));
        System.out.println(pointcut.matches(T1.class.getMethod("bar"), T1.class));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        /**
         * 第二部分是测试T1类中的两个方法，是否满足切点表达式(使用的是注解的方式，例子中是用@Transactional)，使用的是pointcut.matches()这个方法
         */
        AspectJExpressionPointcut pointcut1 = new AspectJExpressionPointcut();
        pointcut1.setExpression("@annotation(org.springframework.transaction.annotation.Transactional)");
        System.out.println(pointcut1.matches(T1.class.getMethod("foo"), T1.class));
        System.out.println(pointcut1.matches(T1.class.getMethod("bar"), T1.class));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        /**
         * 第三部分，是模拟Transactional注解解析，实际情况，这个注解是可以加在类和接口上的，所以使用pointcut.matches()是不行的
         * 这个方法只能够检验加在方法上面的情况
         */
        //模拟Transactional注解解析，因为这个注解可以加在类上和接口上，所以第二个方式不行
        //生成切点，这个切点的实现类和前面不同
        StaticMethodMatcherPointcut pointcut2 = new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                //判断方法上面是否有Transactional注解
                MergedAnnotations annotations = MergedAnnotations.from(method);
                if (annotations.isPresent(Transactional.class)) {
                    return true;
                }
                //判断类上面是否有Transactional注解
                //这个方法第一个参数是查找目标，从targetClass上去查找注解，第二个参数是查找策略，是不光从本类，并且从他的父类和实现的接口去查找
                annotations = MergedAnnotations.from(targetClass, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY);
                if (annotations.isPresent(Transactional.class)) {
                    return true;
                }

                return false;
            }
        };
        //判断目标类是否满足切点表达式
        System.out.println(pointcut2.matches(T1.class.getMethod("foo"), T1.class));
        System.out.println(pointcut2.matches(T2.class.getMethod("foo"), T2.class));
        System.out.println(pointcut2.matches(T3.class.getMethod("foo"), T3.class));

    }

    /**
     * T1,T2,T3类，分别是模拟了，方法上，类上，和接口上分别标注了@Transactional注解的情况
     */

    static class T1 {
        @Transactional
        public void foo() {

        }

        public void bar() {

        }
    }

    @Transactional
    static class T2 {
        public void foo() {
        }
    }

    @Transactional
    interface I3 {
        void foo();
    }

    static class T3 implements I3 {

        @Override
        public void foo() {

        }
    }


}
