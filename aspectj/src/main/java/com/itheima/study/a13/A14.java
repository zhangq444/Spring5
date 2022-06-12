package com.itheima.study.a13;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 这个是模拟Ciglib的方式生成动态代理，是通过继承的方式实现的
 */
public class A14 {

    public static void main(String[] args) {
        /**
         * 代理对象
         */
        Proxy proxy = new Proxy();
        /**
         * 目标对象
         */
        Target target = new Target();
        /**
         * 代理对象设置增强方法，模拟Ciglib的方式
         */
        proxy.setMethodInterceptor(new MethodInterceptor() {
            @Override
            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                System.out.println("======before");
//                return method.invoke(target,args);//这个属于反射调用
                return methodProxy.invoke(target, args);// 内部无反射，结合目标用(spring用的是这一种)
//                return methodProxy.invokeSuper(proxy,args);// 内部无反射，结合代理用
            }
        });
        /**
         * 代理对象调用方法，看是否增强了
         */
        proxy.save();
        proxy.save(1);
        proxy.save(2L);
    }


}

