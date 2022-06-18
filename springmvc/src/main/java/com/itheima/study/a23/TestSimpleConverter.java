package com.itheima.study.a23;

import org.springframework.beans.SimpleTypeConverter;

import java.util.Date;

/**
 * 这一讲是展示类型转换
 */
public class TestSimpleConverter {

    public static void main(String[] args) {

        //仅有类型转换功能
        SimpleTypeConverter converter = new SimpleTypeConverter();
        Integer number = converter.convertIfNecessary("13", int.class);
        Date date = converter.convertIfNecessary("1999/12/14", Date.class);
        System.out.println(number);
        System.out.println(date);
    }


}
