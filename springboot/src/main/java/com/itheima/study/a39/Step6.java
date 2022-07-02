package com.itheima.study.a39;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * 这一讲首先将如何将我们Environment对象中的键和值，和我们的一个实体类对象，例如User，进行数据绑定
 * 然后是讲了，run方法第6步，是将我们配置文件properties中的键值信息(以spring.main开头的配置)，和SpringApplication对象进行绑定
 * @author grzha
 */
@Slf4j
public class Step6 {

    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication();
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addLast(new ResourcePropertySource("step4",new ClassPathResource("step4.properties")));
        env.getPropertySources().addLast(new ResourcePropertySource("step6",new ClassPathResource("step6.properties")));

        //使用Binder进行数据绑定，首先是要从哪里去获得数据源，所以传入env，然后要告诉数据源的前缀，就是properties文件中以user开头，
        //然后是告诉要绑定成什么类型的对象，所以传入User.class
        User user = Binder.get(env).bind("user", User.class).get();
        log.info("======user:{}",user);

        //这个不是在内部创建user对象，而是将一个已有的对象进行数据绑定
        User user1 = new User();
        Binder.get(env).bind("user", Bindable.ofInstance(user1));
        log.info("======user1:{}",user1);

        //由于step6.properties中配置的属性在SpringApplication都是私有的，所以后面只能够debug然后打断点看
        log.info("======绑定前application:{}", application);
        Binder.get(env).bind("spring.main", Bindable.ofInstance(application));
        log.info("======绑定后application:{}", application);

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class User{
        private String firstName;
        private String middleName;
        private String lastName;
    }




}
