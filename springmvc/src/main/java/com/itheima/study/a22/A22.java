package com.itheima.study.a22;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 这一讲将如何获取方法中的参数变量名，这一讲涉及到不同的编译方法，不用idea编译，然后不同的编译方式，有一些会将参数名name，age之类
 * 变成arg0,arg1这样，这样就没法获取参数名了，有的方式编译会生成参数表或者本地变量表，这样就可以获取参数名，这一讲内容不是很重要，
 * 主要是知道是可以获取参数的名称的
 */
public class A22 {

    public static void main(String[] args) throws NoSuchMethodException {
        Method foo = Bean2.class.getDeclaredMethod("foo", String.class, int.class);
        // 第一种方式，通过反射获取参数名
        Arrays.stream(foo.getParameters()).forEach(parameter -> {
            System.out.println(parameter.getName());
        });

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        //  第二种方式，通过LocalVariableTable 本地变量表来获取，这个好像是基于asm技术
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(foo);
        Arrays.stream(parameterNames).forEach(System.out::println);




    }








}
