package com.itheima.study.a12;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

public class $Proxy0 extends Proxy implements A13.Foo {

//    private InvocationHandler handler;

    public $Proxy0(InvocationHandler handler) {
//        this.handler = handler;
        super(handler);
    }

    private static Method foo;
    private static Method bar;

    static {
        try {
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
