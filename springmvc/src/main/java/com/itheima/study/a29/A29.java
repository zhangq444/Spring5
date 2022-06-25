package com.itheima.study.a29;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * 这一讲主要讲ResponseBodyAdvice，自定义返回值处理器，统一处理返回值
 * @author grzha
 */
public class A29 {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);

        //处理器
        ServletInvocableHandlerMethod handlerMethod = new ServletInvocableHandlerMethod(context.getBean(WebConfig.MyController.class), WebConfig.MyController.class.getMethod("user"));
        //设置数据绑定器
        handlerMethod.setDataBinderFactory(new ServletRequestDataBinderFactory(Collections.emptyList(), null));
        //设置参数名解析器
        handlerMethod.setParameterNameDiscoverer(new DefaultParameterNameDiscoverer());
        //设置参数解析器
        handlerMethod.setHandlerMethodArgumentResolvers(getArgumentResolvers(context));
        //设置返回值处理器
        handlerMethod.setHandlerMethodReturnValueHandlers(getReturnValueHandler(context));

        //创建模拟的请求和响应对象
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        //模拟反射调用控制器方法
        handlerMethod.invokeAndHandle(webRequest, new ModelAndViewContainer());

        //输出响应结果
        System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));

        context.close();


    }

    /**
     * 添加常见的参数解析器
     *
     * @param context
     * @return
     */
    public static HandlerMethodArgumentResolverComposite getArgumentResolvers(AnnotationConfigApplicationContext context) {
        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
        composite.addResolvers(
                new RequestParamMethodArgumentResolver(context.getDefaultListableBeanFactory(), false),
                new PathVariableMethodArgumentResolver(),
                new RequestHeaderMethodArgumentResolver(context.getDefaultListableBeanFactory()),
                new ServletCookieValueMethodArgumentResolver(context.getDefaultListableBeanFactory()),
                new ExpressionValueMethodArgumentResolver(context.getDefaultListableBeanFactory()),
                new ServletRequestMethodArgumentResolver(),
                new ServletModelAttributeMethodProcessor(false),
                new RequestResponseBodyMethodProcessor(Lists.newArrayList(new MappingJackson2HttpMessageConverter())),
                new ServletModelAttributeMethodProcessor(true),
                new RequestParamMethodArgumentResolver(context.getDefaultListableBeanFactory(), true)
        );
        return composite;
    }

    /**
     * 将多个返回值处理器整合到一起,这个和老师写的不一样，这个是我抄前面几讲的内容的，老师的视频里面这个返回值处理器的方法没有放出来，
     * 里面要注意，由于返回值是用RequestResponseBodyMethodProcessor，在初始化这个的时候有一个有两个参数的构造方法，第二个参数可以传List<Object> requestResponseBodyAdvice
     * 就是我们前面定义的，所以可以自己new一个，或者从context的容器中获取，这个老师的视频里面没有交，我是自己看了源码悟出来的
     *
     * @return
     */
    public static HandlerMethodReturnValueHandlerComposite getReturnValueHandler(AnnotationConfigApplicationContext context) {
        HandlerMethodReturnValueHandlerComposite composite = new HandlerMethodReturnValueHandlerComposite();
        composite.addHandler(new ModelAndViewMethodReturnValueHandler());
        composite.addHandler(new ViewNameMethodReturnValueHandler());
        composite.addHandler(new ServletModelAttributeMethodProcessor(false));
        composite.addHandler(new HttpEntityMethodProcessor(Lists.newArrayList(new MappingJackson2HttpMessageConverter())));
        composite.addHandler(new HttpHeadersReturnValueHandler());
//        composite.addHandler(new RequestResponseBodyMethodProcessor(Lists.newArrayList(new MappingJackson2HttpMessageConverter()),Lists.newArrayList(new WebConfig.MyControllerAdvice())));
        composite.addHandler(new RequestResponseBodyMethodProcessor(Lists.newArrayList(new MappingJackson2HttpMessageConverter()), Lists.newArrayList(context.getBean(WebConfig.MyControllerAdvice.class))));
        composite.addHandler(new ServletModelAttributeMethodProcessor(true));
        return composite;
    }


}
