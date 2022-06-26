package com.itheima.study.a35;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

/**
 * 这一讲讲了静态资源的映射器和处理器
 *
 * @author grzha
 */
public class A35 {

    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

        /**
         * 总结
         * 一共有5大映射器和4大适配器
         * HandlerMapping 负责建立请求与控制器之间的映射关系
         *
         * RequestMappingHandlerMapping     与@RequestMapping注解匹配
         * WelcomePageHandlerMapping        与/路径匹配，（这个我测试没有成功配置过）
         * BeanNameUrlHandlerMapping        与bean的名字匹配，要以/开头  例如 /c1这样
         * RouterFunctionMapping            函数式，与RequestPredicate匹配
         * SimpleUrlHandlerMapping          静态资源  通配符  /** 或者 /img/**这样的
         * 这些处理器映射器之间存在顺序关系，在springboot中的顺序如上面所示
         *
         *
         * HandlerAdapter 负责实现对各种各样的handler的适配调用
         *
         * RequestMappingHandlerAdapter     处理@RequestMapping的方法
         * SimpleControllerHandlerAdapter   处理实现了Controller接口的处理器
         * HandlerFunctionAdapter           处理函数式接口的请求的处理器
         * HttpRequestHandlerAdapter        处理ResourceHttpRequestHandler，处理静态资源
         *
         */


    }


}
