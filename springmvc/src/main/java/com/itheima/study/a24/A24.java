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
