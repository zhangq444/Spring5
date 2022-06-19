package com.itheima.study.a24;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 这一讲介绍了，全局的数据绑定器和控制器层面的数据绑定器的初始化时间
 */
@Slf4j
public class A24 {

    public static void main(String[] args) throws Exception {
        /**
         *@InitBinder来源有两个
         * 1. 在@ControllerAdvice中，由RequestMappingHandlerAdapter在初始化的时候解析并记录
         * 2. 在Controller中的，由RequestMappingHandlerAdapter在控制器方法首次执行时解析并记录
         */
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);

        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        adapter.setApplicationContext(context);
        adapter.afterPropertiesSet();

        log.info("======1.刚开始");
        showBinderMethods(adapter);

        Method getDataBinderFactory = RequestMappingHandlerAdapter.class.getDeclaredMethod("getDataBinderFactory", HandlerMethod.class);
        getDataBinderFactory.setAccessible(true);

        /**
         * 下面两个所谓模拟调用foo方法和bar方法，实际上并没有真实调用，这个老师讲的时候说是模拟的，但是我在这两个方法中都打印了语句，最后都没有输出，说明foo方法和bar方法并没有执行，
         * 我看了RequestMappingHandlerAdapter的源码，adapter真实在调用控制器方法所使用的的方法是invokeHandlerMethod，但是在这个方法中，调用了getDataBinderFactory方法，
         * 所以，应该是首次执行控制器方法时，调用了invokeHandlerMethod方法，然后调用了getDataBinderFactory，然后初始化了Controller中的@InitBinder，并且存入initBinderCache中去
         */
        log.info("======2. 模拟调用Controller1的foo方法时...");
        getDataBinderFactory.invoke(adapter, new HandlerMethod(new WebConfig.Controller1(), WebConfig.Controller1.class.getMethod("foo")));
        showBinderMethods(adapter);

        log.info("======3. 模拟调用Controller2的bar方法时...");
        getDataBinderFactory.invoke(adapter, new HandlerMethod(new WebConfig.Controller2(), WebConfig.Controller2.class.getMethod("bar")));
        showBinderMethods(adapter);

        context.close();


    }

    private static void showBinderMethods(RequestMappingHandlerAdapter handlerAdapter) throws NoSuchFieldException, IllegalAccessException {
        /**
         * 在@ControllerAdvice中的@InitBinder是存放在RequestMappingHandlerAdapter的initBinderAdviceCache变量中
         */
        Field initBinderAdviceCache = RequestMappingHandlerAdapter.class.getDeclaredField("initBinderAdviceCache");
        initBinderAdviceCache.setAccessible(true);
        Map<ControllerAdviceBean, Set<Method>> globalMap = (Map<ControllerAdviceBean, Set<Method>>) initBinderAdviceCache.get(handlerAdapter);
        log.info("全局的 @InitBinder 方法 {}", globalMap.values().stream().flatMap(methodSet -> methodSet.stream().map(method -> method.getName())).collect(Collectors.toList()));

        /**
         * 在Controller中的@InitBinder是存放在RequestMappingHandlerAdapter的initBinderCache变量中
         */
        Field initBinderCache = RequestMappingHandlerAdapter.class.getDeclaredField("initBinderCache");
        initBinderCache.setAccessible(true);
        Map<Class<?>, Set<Method>> controllerMap = (Map<Class<?>, Set<Method>>) initBinderCache.get(handlerAdapter);
        log.info("控制器的 @InitBinder 方法 {}", controllerMap.entrySet().stream().flatMap(entry -> entry.getValue().stream().map(method -> entry.getKey().getSimpleName() + "." + method.getName())).collect(Collectors.toList()));
    }


}
