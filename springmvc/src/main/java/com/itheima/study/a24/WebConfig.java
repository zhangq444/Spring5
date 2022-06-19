package com.itheima.study.a24;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@Configuration
public class WebConfig {

    /**
     * @ControllerAdvice 虽然名字带advice，和aop的adivce一样，但是和切面没有半毛钱关系
     */
    @ControllerAdvice
    static class MyControllerAdvice{
        /**
         * @InitBinder这个注解可以加在@ControllerAdvice标注的类中,这种是对所有控制器都生效的
         * @param webDataBinder
         */
        @InitBinder
        public void binder3(WebDataBinder webDataBinder){
            webDataBinder.addCustomFormatter(new MyDateFormatter("ControllerAdvice binder3 InitBinder"));
        }
    }

    @Controller
    static class Controller1{
        /**
         * @InitBinder这个注解可以加在controller类中，这个只是对这个控制器生效
         * @param webDataBinder
         */
        @InitBinder
        public void binder1(WebDataBinder webDataBinder){
            webDataBinder.addCustomFormatter(new MyDateFormatter("Controller1 binder1 InitBinder"));
        }

        public void foo(){

        }

    }

    @Controller
    static class Controller2{

        @InitBinder
        public void binder21(WebDataBinder webDataBinder){
            webDataBinder.addCustomFormatter(new MyDateFormatter("Controller2 binder21 InitBinder"));
        }

        @InitBinder
        public void binder22(WebDataBinder webDataBinder){
            webDataBinder.addCustomFormatter(new MyDateFormatter("Controller2 binder22 InitBinder"));
        }

        public void bar(){

        }

    }









}
