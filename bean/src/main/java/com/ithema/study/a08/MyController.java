package com.ithema.study.a08;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class MyController {

    @Lazy
    @Autowired
    private BeanForRequest beanForRequest;

    @Lazy
    @Autowired
    private BeanForSession beanForSession;

    @Lazy
    @Autowired
    private BeanForApplication beanForApplication;


    /**
     * 在拼接字符串sb时，beanForRequest对象和其他对象会转换为字符串，就会调用toString方法，也就会反射调用Object类的toString方法，这样会反射调用jdk的方法，
     * 这个如果在jdk》=9的时候，就会报错，报访问异常，所以要么就是每个类重新toString方法，或者就是增加VM参数，--add-opens java.base/java.lang=ALL-UNNAMED
     * @param request
     * @param session
     * @return
     */

    @GetMapping(value = "/test",produces = "text/html")
    public String test(HttpServletRequest request, HttpSession session){
        ServletContext servletContext = request.getServletContext();
        String sb="<ul>"+
                "<li>"+"request scope:"+beanForRequest+"</li>"+
                "<li>"+"session scope:"+beanForSession+"</li>"+
                "<li>"+"application scope:"+beanForApplication+"</li>"+
                "</ul>";
        return sb;

    }





}
