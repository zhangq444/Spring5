package com.itheima.study.a11;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class JdkProxyDemo {

    interface Foo{
        void foo();
    }

    static class Target implements Foo{

        @Override
        public void foo() {
            log.info("======target foo");
        }
    }

    public static void main(String[] args) {
        Target target = new Target();

        ClassLoader classLoader = JdkProxyDemo.class.getClassLoader();
        Foo proxy = (Foo) Proxy.newProxyInstance(classLoader, new Class[]{Foo.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                log.info("======before");
                Object result = method.invoke(target, args);
                log.info("======after");
                return result;
            }
        });

        proxy.foo();
    }



}
