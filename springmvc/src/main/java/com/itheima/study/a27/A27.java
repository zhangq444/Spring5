package com.itheima.study.a27;

import com.google.common.collect.Lists;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.*;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.springframework.web.util.UrlPathHelper;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 这一张主要讲的是各种返回值处理器，解析各种类型的返回值，有些会经过视图解析器渲染（本讲中是使用freemarker来进行渲染），有些就不经过视图解析器，
 *
 * @author grzha
 */
@Slf4j
public class A27 {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);
//        test1(context);
//        test2(context);
//        test3(context);
//        test4(context);
//        test5(context);
//        test6(context);
        test7(context);


        //这个是我自己网上找的，解析freemarker模板然后输出到控制台的方法
//        freemarkerTest();


    }

    /**
     * 模拟springmvc在获得返回值以后，进行视图渲染的逻辑
     *
     * @param context
     * @param container
     * @param webRequest
     * @throws Exception
     */
    private static void renderView(AnnotationConfigApplicationContext context, ModelAndViewContainer container, ServletWebRequest webRequest) throws Exception {
        log.info("======渲染视图");
        FreeMarkerViewResolver resolver = context.getBean(FreeMarkerViewResolver.class);
        String viewName = container.getViewName() != null ? container.getViewName() : new DefaultRequestToViewNameTranslator().getViewName(webRequest.getRequest());
        log.info("======没有获取到视图名，采用默认视图名:{}", viewName);
        View view = resolver.resolveViewName(viewName, Locale.getDefault());
        view.render(container.getModel(), webRequest.getRequest(), webRequest.getResponse());
        System.out.println(new String(((MockHttpServletResponse) webRequest.getResponse()).getContentAsByteArray(), StandardCharsets.UTF_8));
    }

    private static void test7(AnnotationConfigApplicationContext context) throws Exception {
        Method method = Controller.class.getMethod("test7");

        Controller controller = new Controller();
        Object returnValue1 = method.invoke(controller);

        //包装一个HandlerMethod对象，composite在解析时作为参数使用
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        //数据模型的容器，存放模型数据使用
        ModelAndViewContainer container = new ModelAndViewContainer();

        HandlerMethodReturnValueHandlerComposite composite = getReturnValueHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        //看是否支持解析这一类返回值
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(returnValue1, handlerMethod.getReturnType(), container, webRequest);
            //查看容器中的模型数据
            System.out.println(container.getModel());
            //查看视图名称是否正确
            System.out.println(container.getViewName());
            //判断请求是否被处理过了，没有被处理过才去渲染视图
            if (!container.isRequestHandled()) {
                renderView(context, container, webRequest);
            } else {
                //打印响应头
                response.getHeaderNames().stream().forEach(headName -> {
                    log.info("======name:{},value:{}", headName, response.getHeader(headName));
                });
                //将相应结果先获得字符数组，再转化为字符串再打印
                System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
            }
        }
    }

    private static void test6(AnnotationConfigApplicationContext context) throws Exception {
        Method method = Controller.class.getMethod("test6");

        Controller controller = new Controller();
        Object returnValue1 = method.invoke(controller);

        //包装一个HandlerMethod对象，composite在解析时作为参数使用
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        //数据模型的容器，存放模型数据使用
        ModelAndViewContainer container = new ModelAndViewContainer();

        HandlerMethodReturnValueHandlerComposite composite = getReturnValueHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        //看是否支持解析这一类返回值
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(returnValue1, handlerMethod.getReturnType(), container, webRequest);
            //查看容器中的模型数据
            System.out.println(container.getModel());
            //查看视图名称是否正确
            System.out.println(container.getViewName());
            //判断请求是否被处理过了，没有被处理过才去渲染视图
            if (!container.isRequestHandled()) {
                renderView(context, container, webRequest);
            } else {
                //打印响应头
                response.getHeaderNames().stream().forEach(headName -> {
                    log.info("======name:{},value:{}", headName, response.getHeader(headName));
                });
            }
        }
    }


    private static void test5(AnnotationConfigApplicationContext context) throws Exception {
        Method method = Controller.class.getMethod("test5");

        Controller controller = new Controller();
        Object returnValue1 = method.invoke(controller);

        //包装一个HandlerMethod对象，composite在解析时作为参数使用
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        //数据模型的容器，存放模型数据使用
        ModelAndViewContainer container = new ModelAndViewContainer();

        HandlerMethodReturnValueHandlerComposite composite = getReturnValueHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        //看是否支持解析这一类返回值
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(returnValue1, handlerMethod.getReturnType(), container, webRequest);
            //查看容器中的模型数据
            System.out.println(container.getModel());
            //查看视图名称是否正确
            System.out.println(container.getViewName());
            //判断请求是否被处理过了，没有被处理过才去渲染视图
            if (!container.isRequestHandled()) {
                renderView(context, container, webRequest);
            } else {
                //将相应结果先获得字符数组，再转化为字符串再打印
                System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
            }
        }
    }

    private static void test4(AnnotationConfigApplicationContext context) throws Exception {
        Method method = Controller.class.getMethod("test4");

        Controller controller = new Controller();
        Object returnValue1 = method.invoke(controller);

        //包装一个HandlerMethod对象，composite在解析时作为参数使用
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        //数据模型的容器，存放模型数据使用
        ModelAndViewContainer container = new ModelAndViewContainer();

        HandlerMethodReturnValueHandlerComposite composite = getReturnValueHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test4");
        //这一步是将上面设置的uri的值存入request域中，这样后面就会把这个uri作为视图的名称使用
        UrlPathHelper.defaultInstance.resolveAndCacheLookupPath(request);
        ServletWebRequest webRequest = new ServletWebRequest(request, new MockHttpServletResponse());
        //看是否支持解析这一类返回值
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(returnValue1, handlerMethod.getReturnType(), container, webRequest);
            //查看容器中的模型数据
            System.out.println(container.getModel());
            //查看视图名称是否正确
            System.out.println(container.getViewName());
            renderView(context, container, webRequest);
        }
    }


    private static void test3(AnnotationConfigApplicationContext context) throws Exception {
        Method method = Controller.class.getMethod("test3");

        Controller controller = new Controller();
        Object returnValue1 = method.invoke(controller);

        //包装一个HandlerMethod对象，composite在解析时作为参数使用
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        //数据模型的容器，存放模型数据使用
        ModelAndViewContainer container = new ModelAndViewContainer();

        HandlerMethodReturnValueHandlerComposite composite = getReturnValueHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test3");
        //这一步是将上面设置的uri的值存入request域中，这样后面就会把这个uri作为视图的名称使用
        UrlPathHelper.defaultInstance.resolveAndCacheLookupPath(request);
        ServletWebRequest webRequest = new ServletWebRequest(request, new MockHttpServletResponse());
        //看是否支持解析这一类返回值
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(returnValue1, handlerMethod.getReturnType(), container, webRequest);
            //查看容器中的模型数据
            System.out.println(container.getModel());
            //查看视图名称是否正确
            System.out.println(container.getViewName());
            renderView(context, container, webRequest);
        }
    }

    private static void test2(AnnotationConfigApplicationContext context) throws Exception {
        Method method = Controller.class.getMethod("test2");

        Controller controller = new Controller();
        Object returnValue1 = method.invoke(controller);

        //包装一个HandlerMethod对象，composite在解析时作为参数使用
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        //数据模型的容器，存放模型数据使用
        ModelAndViewContainer container = new ModelAndViewContainer();

        HandlerMethodReturnValueHandlerComposite composite = getReturnValueHandler();
        ServletWebRequest webRequest = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
        //看是否支持解析这一类返回值
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(returnValue1, handlerMethod.getReturnType(), container, webRequest);
            //查看容器中的模型数据
            System.out.println(container.getModel());
            //查看视图名称是否正确
            System.out.println(container.getViewName());
            renderView(context, container, webRequest);
        }
    }


    /**
     * 这个是我自己网上找的，解析freemarker模板然后输出到控制台的方法，其实用我自己找的方法，也可以渲染视图
     *
     * @throws IOException
     * @throws TemplateException
     */
    private static void freemarkerTest() throws IOException, TemplateException {
        //创建一个模板文件
        //创建一个Configuration对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //设置模板文件保存的目录
        String path = A27.class.getClassLoader().getResource("").getPath();
        configuration.setDirectoryForTemplateLoading(new File(path));
        //模板文件的编码格式，一般就是utf-8
        configuration.setDefaultEncoding("utf-8");
        //加载一个模板文件，创建一个模板对象。
        Template template = configuration.getTemplate("hello1.ftl");
        //创建一个数据集。使用map
        Map data = new HashMap<>();
        data.put("hello", "FreemarkerTestHello");
        //添加日期类型,取时间
        data.put("date", new Date());
        //null值的测试
        data.put("val1", null);
        data.put("val2", "222");
        data.put("val3", "333");
        //创建一个Writer对象，指定输出文件的路径及文件名。
//        Writer out = new FileWriter(new File("D:/student.html"));
        Writer out = new OutputStreamWriter(System.out);
        //生成静态页面
        template.process(data, out);
        //关闭流
        out.close();
    }

    private static void test1(AnnotationConfigApplicationContext context) throws Exception {
        Method method = Controller.class.getMethod("test1");

        Controller controller = new Controller();
        Object returnValue1 = method.invoke(controller);

        //包装一个HandlerMethod对象，composite在解析时作为参数使用
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        //数据模型的容器，存放模型数据使用
        ModelAndViewContainer container = new ModelAndViewContainer();

        HandlerMethodReturnValueHandlerComposite composite = getReturnValueHandler();
        ServletWebRequest webRequest = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
        //看是否支持解析这一类返回值
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(returnValue1, handlerMethod.getReturnType(), container, webRequest);
            //查看容器中的模型数据
            System.out.println(container.getModel());
            //查看视图名称是否正确
            System.out.println(container.getViewName());
            renderView(context, container, webRequest);
        }
    }


    /**
     * 将多个返回值处理器整合到一起
     *
     * @return
     */
    public static HandlerMethodReturnValueHandlerComposite getReturnValueHandler() {
        HandlerMethodReturnValueHandlerComposite composite = new HandlerMethodReturnValueHandlerComposite();
        //处理返回值类型为ModelAndView的解析器
        composite.addHandler(new ModelAndViewMethodReturnValueHandler());
        //把返回值当做视图名来解析，就是test2的情况
        composite.addHandler(new ViewNameMethodReturnValueHandler());
        //这个是对应test3，加了@ModelAttribute注解，会把返回值放入模型容器中
        composite.addHandler(new ServletModelAttributeMethodProcessor(false));
        /// 对应test5，可以看HttpEntityMethodProcessor这个处理器中的handleReturnValue方法中，有一段代码mavContainer.setRequestHandled(true);
        /// 这样设置为true之后就是设置请求已经被处理了，就不会走视图解析器了，下面两个HttpHeadersReturnValueHandler和RequestResponseBodyMethodProcessor也是这样设置的
        composite.addHandler(new HttpEntityMethodProcessor(Lists.newArrayList(new MappingJackson2HttpMessageConverter())));
        //对应test6
        composite.addHandler(new HttpHeadersReturnValueHandler());
        //对应test7
        composite.addHandler(new RequestResponseBodyMethodProcessor(Lists.newArrayList(new MappingJackson2HttpMessageConverter())));
        //这个是对应test4，省略了@ModelAttribute注解
        composite.addHandler(new ServletModelAttributeMethodProcessor(true));
        return composite;
    }


    @Slf4j
    static class Controller {

        public ModelAndView test1() {
            log.info("test1()");
            ModelAndView mav = new ModelAndView("view1");
            mav.addObject("name", "张三");
            return mav;
        }

        public String test2() {
            log.info("test2()");
            return "view2";
        }

        /**
         * 会把返回数据放入模型容器中,名字如果不指定，就是返回值类名小写，方法内部没有返回视图名称，会把RequestMapping中的名称作为视图名
         * 现在由于案例不会解析RequestMapping，所以需要手动编程完成
         *
         * @return
         */
        @ModelAttribute
//        @RequestMapping("/test3")
        public User test3() {
            log.info("test3()");
            return User.builder().name("李四").age(20).build();
        }

        public User test4() {
            log.info("test4()");
            return User.builder().name("王五").age(30).build();
        }

        /**
         * 方法5.6.7返回值就直接是响应了，不会走视图渲染的流程
         *
         * @return
         */
        public HttpEntity<User> test5() {
            log.info("test5()");
            return new HttpEntity<>(User.builder().name("赵六").age(40).build());
        }

        public HttpHeaders test6() {
            log.info("test6()");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "text/html");
            return headers;
        }

        @ResponseBody
        public User test7() {
            log.info("test7");
            return User.builder().name("钱七").age(50).build();
        }


    }


    /**
     * 必须用public修饰，否则freemarker渲染其name，age属性时失败
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    public static class User {
        private String name;
        private Integer age;
    }


}
