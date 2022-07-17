package com.ithema.study.a08;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Scope("request")
@Component
@Slf4j
/**
 * toString必须加  不然访问会报错,而且最好加上callSuper = true，这样可以看到对象的hashcode值
 */
@ToString(callSuper = true)
public class BeanForRequest {

    @PreDestroy
    public void destory(){
        log.info("======BeanForRequest被销毁了");
    }


}
