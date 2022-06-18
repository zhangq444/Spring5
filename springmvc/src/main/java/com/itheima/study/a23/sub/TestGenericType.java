package com.itheima.study.a23.sub;

import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 这一讲是23讲里面附加的小内容，主要是讲如何获得类上面的泛型参数，例如StudentDao里面的<Student>这个参数
 */
public class TestGenericType {

    public static void main(String[] args) {
        //1.jdk的api
        Type type = StudentDao.class.getGenericSuperclass();
        System.out.println(type);

        //判断type的类型是不是ParameterizedType，只有当type里面带有泛型的信息，参会是这个类型,例如EmployeeDao就不是这个类型，可以试试
        if(type instanceof ParameterizedType parameterizedType){
            System.out.println(parameterizedType.getActualTypeArguments()[0]);
        }

        // 2. spring的api
        Class<?> clazz = GenericTypeResolver.resolveTypeArgument(StudentDao.class, BaseDao.class);
        System.out.println(clazz);


    }




}
