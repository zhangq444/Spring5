package com.itheima.study.a13;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 代理对象的类，继承目标类
 */
public class Proxy extends Target {

    private MethodInterceptor methodInterceptor;

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    private static Method save0;
    private static Method save1;
    private static Method save2;
    private static MethodProxy save0Proxy;
    private static MethodProxy save1Proxy;
    private static MethodProxy save2Proxy;

    static {
        try {
            /**
             * 初始化原始的目标方法参数，这个就不需要通过反射调用了
             */
            save0 = Target.class.getMethod("save");
            save1 = Target.class.getMethod("save", int.class);
            save2 = Target.class.getMethod("save", long.class);
            /**
             * 初始化方法成员变量的代理，这个就是ciglib中的接口中的第四个参数，
             * 这个是代理类中的方法，也就是在子类中的方法，然后在里面调用父类的方法，就是目标类的方法，这样就不用反射调用了
             */
            save0Proxy=MethodProxy.create(Target.class,Proxy.class,"()V","save","saveSuper");
            save1Proxy=MethodProxy.create(Target.class,Proxy.class,"(I)V","save","saveSuper");
            save2Proxy=MethodProxy.create(Target.class,Proxy.class,"(J)V","save","saveSuper");
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }
    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>带原始功能的方法
    /**
     * 这个是代理类中的方法，也就是在子类中的方法，然后在里面调用父类的方法，就是目标类的方法，这样就不用反射调用了
     */
    public void saveSuper(){
        super.save();
    }

    public void saveSuper(int i){
        super.save(i);
    }

    public void saveSuper(long j){
        super.save(j);
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>带增强功能的方法
    @Override
    public void save() {
        try {
            methodInterceptor.intercept(this, save0, new Object[0], save0Proxy);
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    @Override
    public void save(int i) {
        try {
            methodInterceptor.intercept(this, save1, new Object[]{i}, save1Proxy);
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    @Override
    public void save(long j) {
        try {
            methodInterceptor.intercept(this, save2, new Object[]{j}, save2Proxy);
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}
