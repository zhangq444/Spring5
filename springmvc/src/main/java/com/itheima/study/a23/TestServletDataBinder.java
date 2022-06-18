package com.itheima.study.a23;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

import java.util.Date;

public class TestServletDataBinder {

    public static void main(String[] args) {
        //web环境下数据绑定，这个是从DataBinder衍生过来的，用的都是DataBinder里面的子类，用于web环境
        MyBean target = new MyBean();
        ServletRequestDataBinder binder = new ServletRequestDataBinder(target);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("a","10");
        request.setParameter("b","hello");
        request.setParameter("c","1999/03/04");

        binder.bind(new ServletRequestParameterPropertyValues(request));

        System.out.println(target);


    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    static class MyBean{
        private int a;
        private String b;
        private Date c;
    }

}
