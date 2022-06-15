package com.itheima.study.a20;

import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties({WebMvcProperties.class, ServerProperties.class})//这个注解是参数绑定，里面两个类是分别读取application.properties文件中以spring.mvc开头和server开头的配置，然后会生成对应的Bean，放入容器中
public class WebConfig {
    /**
     * 创建web容器工厂
     * ServerProperties这个参数就是EnableConfigurationProperties注解注入的
     */
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(ServerProperties serverProperties){
        Integer port = serverProperties.getPort();
        return new TomcatServletWebServerFactory(port);
    }

    /**
     * 创建DispatcherServlet
     * DispatcherServlet的初始化不是Spring初始化的，是由Tomcat容器初始化的，是在首次访问DispatcherServlet的时候进行初始化的
     * @return
     */
    @Bean
    public DispatcherServlet dispatcherServlet(){
        return new DispatcherServlet();
    }

    /**
     * 注册DispatcherServlet，SpingMVC的入口
     * WebMvcProperties这个参数就是用EnableConfigurationProperties这个注解注入的
     * @param dispatcherServlet
     * @return
     */
    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet,WebMvcProperties webMvcProperties){
        //两个参数，一个是要注册的DispatcherServlet，一个是访问的路径，以后什么请求路径要经过DispatcherServlet
        DispatcherServletRegistrationBean registrationBean = new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        //下面参数，只要大于0，那么就会在tomcat容器初始化时，同步初始化DispatcherServlet，而这个数字的大小，是如果你有多个Servlet，代表初始化的优先级，越小优先级越高
//        registrationBean.setLoadOnStartup(1);
        int loadOnStartup = webMvcProperties.getServlet().getLoadOnStartup();
        registrationBean.setLoadOnStartup(loadOnStartup);
        return registrationBean;
    }

    /**
     * 加入RequestMappingHandlerMapping
     * 这个是处理器映射器，在DispatcherServlet初始化时，会初始化，但是只是作为成员变量放在DispatcherServlet，现在是将它直接放入spring容器中
     * @return
     */
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping(){
        return new RequestMappingHandlerMapping();
    }

    /**
     * 加入RequestMappingHandlerAdapter
     * 这个是处理器适配器，和处理器映射器一样，在DispatcherServlet初始化时，会初始化，但是只是作为成员变量放在DispatcherServlet，现在是将它直接放入spring容器中
     * 这里配置的是我们写的子类，继承了RequestMappingHandlerAdapter，然后把我们要调用的受保护的方法重写
     * 然后在这个处理器适配器中，增加我们自定义的参数解析器
     * 也在这个处理器适配器中，增加我们自定义的返回值解析器
     * @return
     */
    @Bean
    public MyRequestMappingHandlerAdapter requestMappingHandlerAdapter(){
        TokenArgumentResolver tokenArgumentResolver = new TokenArgumentResolver();
        MyRequestMappingHandlerAdapter handlerAdapter = new MyRequestMappingHandlerAdapter();
        YmlReturnValueHandler returnValueHandler = new YmlReturnValueHandler();
        handlerAdapter.setCustomArgumentResolvers(Lists.newArrayList(tokenArgumentResolver));
        handlerAdapter.setCustomReturnValueHandlers(Lists.newArrayList(returnValueHandler));
        return handlerAdapter;
    }









}
