package com.itheima.study.a32;

import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistrarBeanPostProcessor;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author grzha
 */
@Configuration
public class WebConfig {

    @Bean
    public TomcatServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public DispatcherServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
        return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    /**
     * 默认的RequestMappingHandlerAdapter不会带有json的转换器
     *
     * @return
     */
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        adapter.setMessageConverters(Lists.newArrayList(new MappingJackson2HttpMessageConverter()));
        return adapter;
    }


    /**
     * 改动tomcat默认的错误页面的配置路径
     *
     * @return
     */
    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new ErrorPageRegistrar() {
            /**
             * webServerFactory其实就是TomcatServletWebServerFactory这个上面注册的tomcat的容器，TomcatServletWebServerFactory实现了
             * ErrorPageRegistry的接口，出现错误，会使用请求转发 forward的方式跳转到错误页面或控制器方法
             * @param webServerFactory
             */
            @Override
            public void registerErrorPages(ErrorPageRegistry webServerFactory) {
                //注册一个错误路径，可以使静态页面，也可以是一个控制器方法
                webServerFactory.addErrorPages(new ErrorPage("/error"));
            }
        };
    }

    /**
     * 作用是在tomcat容器初始化时，搜索所有的ErrorPageRegistrar，然后把它添加到TomcatServletWebServerFactory中去，
     * 所以要添加就要同时添加两个bean
     *
     * @return
     */
    @Bean
    public ErrorPageRegistrarBeanPostProcessor errorPageRegistrarBeanPostProcessor() {
        return new ErrorPageRegistrarBeanPostProcessor();
    }


    @Controller
    public static class MyController {

        @RequestMapping("/test")
        public ModelAndView test() {
            int i = 1 / 0;
            return null;
        }

        /**
         * 如果出现异常，tomcat会把异常对象存放在request域中，所以可以在参数中加上request中获取
         * 存储的名字就是RequestDispatcher.ERROR_EXCEPTION
         * @param request
         * @return
         */
        /*@RequestMapping("/error")
        @ResponseBody
        public Map<String,Object> error(HttpServletRequest request){
            Throwable e = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
            return Map.of("error",e.getMessage());
        }*/


    }

    /**
     * 这个是springboot定义的处理异常信息的Controller，配了这个就要把我们自己配的注释掉，因为请求路径冲突了
     *
     * @return
     */
    @Bean
    public BasicErrorController basicErrorController() {
        ErrorProperties errorProperties = new ErrorProperties();
        //用来显示错误信息
        errorProperties.setIncludeException(true);
        return new BasicErrorController(new DefaultErrorAttributes(), errorProperties);
    }

    /**
     * 定义一个错误页面error,就是视图对象，这样浏览器访问的时候就会找这个页面视图对象（postman访问会返回json字符串，因为BasicErrorController处理时用的是不同的方法），注意，这个视图名一定要叫error，就是方法名一定要叫这个
     *
     * @return
     */
    @Bean
    public View error() {
        return new View() {
            @Override
            public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                System.out.println(model);
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().print("""
                        <h3>服务器内部错误</h3>
                        """);
            }
        };
    }

    /**
     * 定义一个视图解析器，根据bean的名称去找View的视图，这样才能够找到上面定义的叫error的视图
     *
     * @return
     */
    @Bean
    public ViewResolver viewResolver() {
        return new BeanNameViewResolver();
    }

}
