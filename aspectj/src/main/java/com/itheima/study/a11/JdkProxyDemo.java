package com.itheima.study.a11;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 通过jdk的动态代理生成代理对象
 */
@Slf4j
public class JdkProxyDemo {

    interface Foo {
        void foo();
    }

    static class Target implements Foo {

        @Override
        public void foo() {
            log.info("======target foo");
        }
    }

    public static void main(String[] args) {
        Target target = new Target();

        /** 类加载器 */
        ClassLoader classLoader = JdkProxyDemo.class.getClassLoader();
        /**
         * 生成代理对象，第一个参数，类加载器，第二个参数，代理对象要实现的接口，第三个参数，要增强的逻辑
         * 在例子中，要增强的逻辑是调用目标对象的方法前，打印before，在调用后打印after，相当于模拟前置通知和后置通知
         */
        Foo proxy = (Foo) Proxy.newProxyInstance(classLoader, new Class[]{Foo.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                log.info("======before");
                /**
                 * 调用目标对象中的对应方法
                 */
                Object result = method.invoke(target, args);
                log.info("======after");
                return result;
            }
        });
        /**
         * 代理对象调用目标方法
         */
        proxy.foo();
    }


}
