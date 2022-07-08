package com.itheima.study.a43;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author grzha
 */
@Slf4j
@Component("bean1")
public class Bean1FactoryBean implements FactoryBean<Bean1> {
    /**
     * 返回所管理的对象
     * @return
     * @throws Exception
     */
    @Override
    public Bean1 getObject() throws Exception {
        Bean1 bean1 = new Bean1();
        log.info("======create bean:{}",bean1);
        return bean1;
    }

    /**
     * FactoryBean所管理的类型
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return Bean1.class;
    }

    /**
     * 管理的bean是否是单例的，决定getObject方法是调用1次还是多次，如果是true，就是只调用1次
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

}
