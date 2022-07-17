package com.ithema.study.a08.a08_2;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author grzha
 */
@Data
@Component
@ToString(callSuper = true)
public class E {

    /**
     * @Lazy是解决单例类中注入时scope失效用的
     */
    @Lazy
    @Autowired
    private F1 f1;

    @Autowired
    private F2 f2;

    /**
     * 这是第三种解决scope失效的方法，使用对象工厂，注入工厂，然后getF3方法，返回f3.getObject()就可以了
     */
    @Autowired
    private ObjectFactory<F3> f3;

    public F3 getF3() {
        return f3.getObject();
    }

    /**
     * 第四种解决办法，直接注入工厂，再在getF4中通过工厂来获取F4对象，那么就是多例的了
     */
    @Autowired
    private ApplicationContext context;

    public F4 getF4(){
        return context.getBean(F4.class);
    }
}
