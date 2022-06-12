package com.itheima.study.a12;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 这个是模拟JDK的动态代理的实现，是需要目标类实现某个接口的
 */
public class A13 {

    /**
     * 目标对象和代理对象要实现的接口
     */
    interface Foo {
        void foo();

        int bar();
    }

    /**
     * 要生成代理对象的目标类
     */
    static class Target implements Foo {

        @Override
        public void foo() {
            System.out.println("======target foo");
        }

        @Override
        public int bar() {
            System.out.println("======target bar");
            return 100;
        }
    }

    /**
     * 这个是增强方法的接口，这个是用来模拟JDK动态代理传的那个InvocationHandler
     *
     * @param args
     */
    /*interface InvocationHandler {
        Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
    }*/
    public static void main(String[] args) {
        /**
         * 目标对象
         */
        Foo target = new Target();
        /**
         * 模拟JDK动态代理自己生成代理对象
         */
        Foo proxy = new $Proxy0(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
                /**
                 * 对目标方法进行增强，模拟实现前置通知
                 */
                System.out.println("======before");
                Object result = method.invoke(target, args);
                return result;
            }
        });
        /**
         * 调用代理对象的方法，看是否被增强
         */
        proxy.foo();
        System.out.println(proxy.bar());

    }


}
