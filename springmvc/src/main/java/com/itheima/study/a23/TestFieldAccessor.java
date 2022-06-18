package com.itheima.study.a23;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.DirectFieldAccessor;

import java.util.Date;

public class TestFieldAccessor {

    public static void main(String[] args) {
        //利用反射的原理，为bean的属性赋值，这个不需要bean有get，set方法
        MyBean target = new MyBean();
        DirectFieldAccessor accessor = new DirectFieldAccessor(target);
        accessor.setPropertyValue("a","10");
        accessor.setPropertyValue("b","hello");
        accessor.setPropertyValue("c","1999/03/04");
        System.out.println(target);

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    static class MyBean{
        private int a;
        private String b;
        private Date c;
    }
}
