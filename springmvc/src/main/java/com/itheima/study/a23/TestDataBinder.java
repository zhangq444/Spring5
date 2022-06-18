package com.itheima.study.a23;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import java.util.Date;

public class TestDataBinder {

    public static void main(String[] args) {
        //执行数据绑定
        MyBean target = new MyBean();
        DataBinder binder = new DataBinder(target);
        // 设置这个数据绑定是走set方法还是走成员变量，默认是false，走的是set方法，如果调了下面那个方法，就会改为true，走的就是成员变量，可以把@Data注解删除试试
        binder.initDirectFieldAccess();
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.add("a","10");
        pvs.add("b","hello");
        pvs.add("c","1999/03/04");
        binder.bind(pvs);
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
