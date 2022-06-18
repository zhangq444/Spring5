package com.itheima.study.a23;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Date;

public class TestBeanWrapper {

    public static void main(String[] args) {
        //利用反射的原理，为bean的属性赋值,会用底层类型转换器的功能，进行类型转换,这个需要bean有get，set方法
        MyBean target = new MyBean();
        BeanWrapperImpl wrapper = new BeanWrapperImpl(target);
        wrapper.setPropertyValue("a","10");
        wrapper.setPropertyValue("b","hello");
        wrapper.setPropertyValue("c","1999/03/04");
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
