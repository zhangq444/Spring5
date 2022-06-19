package com.itheima.study.a26;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class WebConfig {

    @ControllerAdvice
    static class MyControllerAdvice{
        @ModelAttribute("a")//这个是针对全局的
        private String aa(){
            return "aa";
        }
    }


    @Controller
    static class Controller1{
        @ModelAttribute("b")//这个是针对这个Controller的
        private String bb(){
            return "bb";
        }

        @ResponseStatus(HttpStatus.OK)//加了这个注解，HandlerMethodReturnValueHandlerComposite这个就不用写了，就不用处理返回值了，测试的时候方便
        public ModelAndView foo(@ModelAttribute("u") User user){
            System.out.println("foo");
            return null;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class User{
        private String name;
    }



}
