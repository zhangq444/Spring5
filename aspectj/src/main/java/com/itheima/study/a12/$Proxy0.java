package com.itheima.study.a12;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 这个是模拟JDK的动态代理的Proxy类写的一个类，可以生成代理对象，代理对象就是$Proxy0类的对象
 */
public class $Proxy0 extends Proxy implements A13.Foo {

    /**
     * 这个是增强方法的处理器，在这个方法中写增强的逻辑，原来可以自己写，在继承Proxy后，Proxy这个类中有一个已经
     * 定义好的InvocationHandler，我自己看了一下，Proxy里面生成代理对象的方法，是首先获得构造方法，然后用构造方法
     * 反射生成对象的
     * @param handler
     */
//    private InvocationHandler handler;

    public $Proxy0(InvocationHandler handler) {
//        this.handler = handler;
        super(handler);
    }

    private static Method foo;
    private static Method bar;

    static {
        try {
            /**
             * 将接口中的方法赋值给代理对象中定义的方法，我感觉这个就是为什么JDK的动态代理需要把目标对象实现的接口传进来
             */
            foo = A13.Foo.class.getDeclaredMethod("foo");
            bar = A13.Foo.class.getDeclaredMethod("bar");
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }
    }


    @Override
    public void foo() {
        try {
//            handler.invoke(this, foo, new Object[0]);
            h.invoke(this, foo, new Object[0]);
        } catch (RuntimeException | Error error) {
            throw error;
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    @Override
    public int bar() {
        try {
//            Object result = handler.invoke(this, bar, new Object[0]);
            Object result = h.invoke(this, bar, new Object[0]);
            return (int) result;
        } catch (RuntimeException | Error error) {
            throw error;
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }
}
