package com.itheima.study.a11;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Slf4j
public class CglibProxyDemo {

    static class Target{
        public void foo(){
            log.info("======target foo");
        }
    }

    public static void main(String[] args) {
        Target target = new Target();

        Target proxy = (Target) Enhancer.create(Target.class, new MethodInterceptor() {
            @Override
            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                log.info("======before");
                //这个是反射调用
//                Object result = method.invoke(target, args);
                //使用methodProxy可以避免反射
                Object result = methodProxy.invoke(target, args);//这个方法内部不是使用反射(spring用的是这一种)
//                Object result = methodProxy.invokeSuper(proxy, args);//这个方法内部也没有使用反射，这样也可以

                log.info("======after");
                return result;
            }
        });
        proxy.foo();

    }




}
