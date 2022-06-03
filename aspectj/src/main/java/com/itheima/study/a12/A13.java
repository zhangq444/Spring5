package com.itheima.study.a12;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class A13 {

    interface Foo {
        void foo();

        int bar();
    }

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

    /*interface InvocationHandler {
        Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
    }*/


    public static void main(String[] args) {
        Foo proxy = new $Proxy0(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
                System.out.println("======before");
                Object result = method.invoke(new Target(), args);
                return result;
            }
        });
        proxy.foo();
        System.out.println(proxy.bar());

    }


}
