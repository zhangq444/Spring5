package com.itheima.study.a21;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 这一讲主要讲了各类参数解析器，是用来将请求过来的各种参数，解析到controller方法的参数中去
 */
@Slf4j
public class A21 {

    public static void main(String[] args) throws NoSuchMethodException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();

        //准备测试request
        HttpServletRequest request = mockRequest();

        //1. 控制器方法被封装为HandlerMethod, 两个参数一个是controller对象，另外一个是控制器方法对象，这样就可以用method的invoke方法了，反射调用
        HandlerMethod handlerMethod = new HandlerMethod(new Controller(), Controller.class.getMethod("test", String.class, String.class, int.class, String.class, MultipartFile.class, int.class, String.class, String.class, String.class, HttpServletRequest.class, User.class, User.class, User.class));

        //2. 准备对象绑定与类型转换,例如将String的18转换为int的18
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, null);


        //3.  准备ModelAndViewContainer对象，用来存储中间产生的model数据
        ModelAndViewContainer container = new ModelAndViewContainer();

        //4.  解析每个参数
        Arrays.stream(handlerMethod.getMethodParameters()).forEach(methodParameter -> {
            //用来解析@RequestParam这个注解，第一个参数是beanFactory，第二个参数是boolean值，true代表可以省略，false代表不能够省略
            RequestParamMethodArgumentResolver resolver1 = new RequestParamMethodArgumentResolver(beanFactory, false);
            //用来解析@PathVariable这个注解
            PathVariableMethodArgumentResolver resolver2 = new PathVariableMethodArgumentResolver();
            //用来解析请求头的，@RequestHeader注解
            RequestHeaderMethodArgumentResolver resolver3 = new RequestHeaderMethodArgumentResolver(beanFactory);
            //用来解析@CookieValue注解
            ServletCookieValueMethodArgumentResolver resolve4 = new ServletCookieValueMethodArgumentResolver(beanFactory);
            //用来解析@Value，就是从spring容器中去获取
            ExpressionValueMethodArgumentResolver resolver5 = new ExpressionValueMethodArgumentResolver(beanFactory);
            //根据HttpServletRequest的类型来解析参数
            ServletRequestMethodArgumentResolver resolver6 = new ServletRequestMethodArgumentResolver();
            //解析@ModelAttribute注解，构造方法传false就是代表这个注解是必须有的
            ServletModelAttributeMethodProcessor resolver7 = new ServletModelAttributeMethodProcessor(false);
            //解析省略了@ModelAttribute注解的对象参数，构造方法传true就是代表这个注解不是必须有的，就是直接转成对象,这是spring的处理方式
            ServletModelAttributeMethodProcessor resolver8 = new ServletModelAttributeMethodProcessor(true);
            //解析@RequestBody注解的
            RequestResponseBodyMethodProcessor resolver9 = new RequestResponseBodyMethodProcessor(Lists.newArrayList(new MappingJackson2HttpMessageConverter()));
            //用来解析@RequestParam这个注解，第一个参数是beanFactory，第二个参数是boolean值，true代表可以省略，false代表不能够省略,这个是用来解析name2这个参数的，什么注解都没有加，也不是对象
            RequestParamMethodArgumentResolver resolver10 = new RequestParamMethodArgumentResolver(beanFactory, true);
            // ServletModelAttributeMethodProcessor解析器在解析了之后，生成User对象，会把这个对象存放入前面的ModelAndViewContainer容器中


            //多个解析器的组合
            HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
            //这个添加有顺序，resolver9要在resolver8之前，否则他会把@RequestBody标注的对象当做省略了@ModelAttribute注解的对象来解析,同理，resolver10要放在resolver8，优先处理对象，后处理单个参数
            composite.addResolvers(resolver1, resolver2, resolver3, resolve4, resolver5, resolver6, resolver7, resolver9, resolver8, resolver10);

            //获取参数的注解，获取注解的类型名称
            String name = Arrays.stream(methodParameter.getParameterAnnotations()).map(annotation -> {
                return annotation.annotationType().getSimpleName();
            }).collect(Collectors.joining());
            if (StringUtils.isNotBlank(name)) {
                name = "@" + name + " ";
            }

            //设置参数名的解析器，用来下面打印参数名
            methodParameter.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());

            //判断解析器是否支持解析当前参数
            if (composite.supportsParameter(methodParameter)) {
                Object value = null;
                try {
                    value = composite.resolveArgument(methodParameter, container, new ServletWebRequest(request), factory);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("======methodParameter, index:{}  {}  {} -> {} ", methodParameter.getParameterIndex(), name + methodParameter.getParameterType().getSimpleName(), methodParameter.getParameterName(), value);
                log.info("======模型数据:{}", container.getModel());
            } else {
                log.info("======methodParameter, index:{}  {}  {} ", methodParameter.getParameterIndex(), name + methodParameter.getParameterType().getSimpleName(), methodParameter.getParameterName());
            }


        });


    }

    private static HttpServletRequest mockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name1", "zhangsan");
        request.setParameter("name2", "lisi");
        request.addPart(new MockPart("file", "abc", "hello".getBytes(StandardCharsets.UTF_8)));
        Map<String, String> map = new AntPathMatcher().extractUriTemplateVariables("/test/{id}", "/test/123");
        request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, map);
        request.setContentType("application/json");
        request.setCookies(new Cookie("token", "123456"));
        request.setParameter("name", "张三");
        request.setParameter("age", "18");
        request.setContent("""
                {
                    "name":"李四",
                    "age":20
                }
                """.getBytes(StandardCharsets.UTF_8));

        return new StandardServletMultipartResolver().resolveMultipart(request);
    }


    static class Controller {
        public void test(
                @RequestParam("name") String name1,//解析name1=张三有这种参数的，可能是拼在url后面，也有可能是通过表单提交过来的
                String name2,//和第一种一样
                @RequestParam("age") int age,//这个涉及一个类型转换
                @RequestParam(name = "home", defaultValue = "${JAVA_HOME}") String home1,//spring获取数据
                @RequestParam("file") MultipartFile file,//上传文件
                @PathVariable("id") int id,//路径参数  /test/123  /test/{id}
                @RequestHeader("Content-Type") String header,//解析header
                @CookieValue("token") String token,//解析cookie的数据
                @Value("${JAVA_HOME}") String home2,//参数不一定从请求中来，也可以从spring中来
                HttpServletRequest request,//特殊类型的参数  request,response,session...
                @ModelAttribute("abc") User user1,// name=张三&age=18这种类型的参数，或者表单,   封装成对象 abc是指定模型数据存入容器中的名字
                User user2,//和ModelAttribute一样
                @RequestBody User user3 // 从请求体里面获取数据，json格式的
        ) {

        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class User {
        private String name;
        private Integer age;
    }


}
