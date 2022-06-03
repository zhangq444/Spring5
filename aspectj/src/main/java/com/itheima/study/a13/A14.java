package com.itheima.study.a13;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class A14 {

    public static void main(String[] args) {
        Proxy proxy = new Proxy();
        Target target = new Target();
        proxy.setMethodInterceptor(new MethodInterceptor() {
            @Override
            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                System.out.println("======before");
//                return method.invoke(target,args);//这个属于反射调用
//                return methodProxy.invoke(target,args);// 内部无反射，结合目标用
                return methodProxy.invokeSuper(proxy,args);// 内部无反射，结合代理用
            }
        });

        proxy.save();
        proxy.save(1);
        proxy.save(2L);
    }


}

