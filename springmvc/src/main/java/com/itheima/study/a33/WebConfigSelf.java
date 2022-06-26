package com.itheima.study.a33;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.Controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 这个配置类里面要自己写一个handlerMapping和handlerAdapter
 *
 * @author grzha
 */
@Configuration
public class WebConfigSelf {

    /**
     * 内嵌web容器
     *
     * @return
     */
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory(8080);
    }

    /**
     * 创建DispatcherServlet
     *
     * @return
     */
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    /**
     * 注册DispatcherServlet到web容器，SpringMVC的入口
     *
     * @param dispatcherServlet
     * @return
     */
    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet) {
        return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
    }

    /**
     * 自定义的处理器映射器
     */
    @Component
    static class MyHandlerMapping implements HandlerMapping {

        @Override
        public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
            String requestURI = request.getRequestURI();
            Controller controller = collect.get(requestURI);
            if (Objects.isNull(controller)) {
                return null;
            }
            return new HandlerExecutionChain(controller);
        }

        @Autowired
        private ApplicationContext context;
        private Map<String, Controller> collect;

        /**
         * 依赖注入在前，初始化在后，所以调用init方法时，ApplicationContext已经被注入了
         */
        @PostConstruct
        public void init() {
            Map<String, Controller> map = context.getBeansOfType(Controller.class);
            collect = map.entrySet().stream().filter(entry -> entry.getKey().startsWith("/")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            System.out.println(collect);
        }

    }

    /**
     * 自定义的处理器适配器
     */
    @Component
    static class MyHandlerAdapter implements HandlerAdapter {

        /**
         * 判断当前传过来的handler是否实现了Controller接口，我们默认我们的处理器适配器只处理实现了Controller接口的请求
         * 如果返回true，就会执行下面那个handle方法
         *
         * @param handler
         * @return
         */
        @Override
        public boolean supports(Object handler) {
            return handler instanceof Controller;
        }

        /**
         * 将handler强转成Controller，然后调用里面的handleRequest方法，传入参数request和response，
         * 然后返回null，这样就不会去走视图解析器的流程了
         *
         * @param request
         * @param response
         * @param handler
         * @return
         * @throws Exception
         */
        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            if (handler instanceof Controller controller) {
                controller.handleRequest(request, response);
            }
            return null;
        }

        /**
         * 这个方法已经被废弃了，只要返回-1就可以了，不用管它，老师视频讲的
         *
         * @param request
         * @param handler
         * @return
         */
        @Override
        public long getLastModified(HttpServletRequest request, Object handler) {
            return -1;
        }
    }


    @Component("/c1")
    public static class Controller1 implements Controller {

        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().print("this is c1");
            return null;
        }
    }

    /**
     * 这个控制器的名字不是以/打头的，所以不会被调用
     */
    @Component("c2")
    public static class Controller2 implements Controller {

        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().print("this is c2");
            return null;
        }
    }

    @Bean("/c3")
    public Controller controller3() {
        return (request, response) -> {
            response.getWriter().print("this is c3");
            return null;
        };
    }


}
