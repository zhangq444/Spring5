package com.itheima.study.a11;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 模拟cglib生成代理对象的过程
 */
@Slf4j
public class CglibProxyDemo {

    /**
     * 目标类中的方法
     */
    static class Target {
        public void foo() {
            log.info("======target foo");
        }
    }

    public static void main(String[] args) {
        Target target = new Target();
        /**
         * 通过cglib的方式生成代理对象
         */
        Target proxy = (Target) Enhancer.create(Target.class, new MethodInterceptor() {
            @Override
            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                log.info("======before");
                //这个是反射调用
//                Object result = method.invoke(target, args);
                //使用methodProxy可以避免反射
                Object result = methodProxy.invoke(target, args);//这个方法内部不是使用反射(spring用的是这一种)
//                Object result = methodProxy.invokeSuper(proxy, args);//这个方法内部也没有使用反射，这样也可以，这样就不需要用目标对象了

                log.info("======after");
                return result;
            }
        });
        /**
         * 代理对象执行目标方法
         */
        proxy.foo();

    }


}
