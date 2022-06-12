package com.itheima.study.a18;

import com.google.common.collect.Lists;
import lombok.Data;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 这个类是用来模拟前面的调用链，来递归调用所有的通知，形成环绕通知
 */
public class A18_1 {

    /**
     * 目标类
     */
    static class Target {

        /*public void foo(){
            System.out.println("target foo()...");
        }*/

        /**
         * 老师的视频是没有返回值的，有返回值的这个是我自己试的，看看最终结果
         *
         * @return
         */
        public int foo() {
            System.out.println("target foo()...");
            return 100;
        }
    }

    /**
     * 环绕通知1
     */
    static class Advice1 implements MethodInterceptor {

        @Nullable
        @Override
        public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
            System.out.println("Advice1 before");
            Object result = invocation.proceed();
            System.out.println("Advice1 after");
            return result;
        }
    }

    /**
     * 环绕通知2
     */
    static class Advice2 implements MethodInterceptor {

        @Nullable
        @Override
        public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
            System.out.println("Advice2 before");
            Object result = invocation.proceed();
            System.out.println("Advice2 after");
            return result;
        }
    }

    /**
     * 模拟生成的调用链
     */
    @Data
    static class MyInvocation implements MethodInvocation {

        private Object target;//目标对象
        private Method method;//目标对象要调用的方法
        private Object[] args;//目标对象调用的方法的参数
        private List<MethodInterceptor> methodInterceptorList;//环绕通知的list对象
        private int count = 1;

        public MyInvocation(Object target, Method method, Object[] args, List<MethodInterceptor> methodInterceptorList) {
            this.target = target;
            this.method = method;
            this.args = args;
            this.methodInterceptorList = methodInterceptorList;
        }

        @Nonnull
        @Override
        public Method getMethod() {
            return method;
        }

        @Nonnull
        @Override
        public Object[] getArguments() {
            return args;
        }

        @Nullable
        @Override
        public Object proceed() throws Throwable {
            if (count > methodInterceptorList.size()) {
                //调用目标，返回并结束递归
                return method.invoke(target, args);
            }
            //逐一调用通知 ，count+1
            MethodInterceptor methodInterceptor = methodInterceptorList.get(count++ - 1);
            return methodInterceptor.invoke(this);
        }

        @Nullable
        @Override
        public Object getThis() {
            return target;
        }

        @Nonnull
        @Override
        public AccessibleObject getStaticPart() {
            return method;
        }
    }

    /**
     * 整个调用链的过程是这样的，首先执行invocation.proceed()这一行代码，然后会检测count，这是count是1，小于通知的总数2，所以不会退出，
     * 然后就会取得第一个环绕通知，然后count加1，然后执行环绕通知的invoke方法，并将调用链本身当参数传入，
     * 然后执行第一个环绕通知的System.out.println("Advice1 before");这段代码，然后就执行传入的调用链的proceed()方法，检测count，这时
     * count是2，不大于通知总数2，也不会退出，取得第二个环绕通知，count在加1，然后执行环绕通知的invoke方法，并将调用链本身当参数传入，
     * 然后执行第二个环绕通知的System.out.println("Advice2 before");这段代码，然后就执行传入的调用链的proceed()方法，检测count，这时
     * count是3，大于通知总数2，这时会退出，会根据目标对象和参数，执行目标方法，然后返回，
     * 然后执行第二个环绕通知的System.out.println("Advice2 after");这段代码，然后继续退出，
     * 然后执行第一个环绕通知的System.out.println("Advice1 after");这段代码，然后就全部退出了
     * 这样递归调用就完成了，整个过程执行了目标方法，然后对目标方法进行了增强，增强了两个环绕通知的代码。
     *
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {
        //创建目标对象
        Target target = new Target();
        //创建通知集合
        List<MethodInterceptor> list = Lists.newArrayList(new Advice1(), new Advice2());
        //创建调用链
        MyInvocation invocation = new MyInvocation(target, Target.class.getMethod("foo"), new Object[0], list);
        //执行调用链
        Object result = invocation.proceed();
        System.out.println(result);

    }


}
