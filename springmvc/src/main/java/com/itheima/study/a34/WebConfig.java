package com.itheima.study.a34;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.function.*;
import org.springframework.web.servlet.function.support.HandlerFunctionAdapter;
import org.springframework.web.servlet.function.support.RouterFunctionMapping;

/**
 * @author grzha
 */
@Configuration
public class WebConfig {


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
     * spring在5.2版本才引入的，功能类似于处理器映射器
     * 在初始化的时候，他会去容器里面找到所有的RouterFunction，然后把它们记录下来，包括匹配路径和处理逻辑
     * 然后等到一个请求来了，就会根据请求路径去匹配RouterFunction，找到路径和请求方式一样的，然后获取处理器对象
     * 然后交给HandlerFunctionAdapter来处理
     *
     * @return
     */
    @Bean
    public RouterFunctionMapping routerFunctionMapping() {
        return new RouterFunctionMapping();
    }

    /**
     * spring在5.2版本才引入的，功能类似于处理器适配器，和上面那个RouterFunctionMapping一起配套使用
     *  它会在请求来时，把ServerRequest请求参数准备好，然后执行RouterFunctionMapping交给他的处理器对象，然后把RouterFunction
     *  返回的响应返回给浏览器
     * @return
     */
    @Bean
    public HandlerFunctionAdapter handlerFunctionAdapter() {
        return new HandlerFunctionAdapter();
    }

    /**
     * 配置RouterFunction,类似于控制器方法，相当于Controller中的方法
     * 其中，RouterFunction包含两个部分，一个是匹配的请求路径，一个是处理的逻辑
     * RequestPredicates.GET("r1")代表请求路径，是get请求，并且路径为/r1
     * HandlerFunction代表处理逻辑，里面给了ServerRequest作为请求的参数，ServerResponse作为相应结果
     */
    @Bean
    public RouterFunction<ServerResponse> routerFunction1() {
        RouterFunction<ServerResponse> route = RouterFunctions.route(RequestPredicates.GET("/r1"), new HandlerFunction<ServerResponse>() {
            @Override
            public ServerResponse handle(ServerRequest request) throws Exception {
                return ServerResponse.ok().body("this is routerFunction1");
            }
        });
        return route;
    }

    /**
     * 这个和上面那个一样，只是用lambda表达式简化了
     *
     * @return
     */
    @Bean
    public RouterFunction<ServerResponse> routerFunction2() {
        return RouterFunctions.route(RequestPredicates.GET("/r2"), request -> ServerResponse.ok().body("this is routerFunction2"));
    }


}
