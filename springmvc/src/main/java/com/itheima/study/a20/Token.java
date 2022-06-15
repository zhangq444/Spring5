package com.itheima.study.a20;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)//这个注解是加在参数上的
@Retention(RetentionPolicy.RUNTIME)//这个注解一直在运行期都是有效的
public @interface Token {

}
