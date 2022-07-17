package com.ithema.study.a06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Arrays;

/**
 *  这一讲讲的是Aware接口和InitializingBean接口
 * @author grzha
 */
@Slf4j
public class A06Application {

    public static void main(String[] args) {
        /**
         * 1.Aware  接口用于注入一些与容器相关信息，例如
         *      a.BeanNameAware  注入bean的名字
         *      b.BeanFactoryAware  注入BeanFactory容器
         *      c.ApplicationContextAware  注入ApplicationContext 容器
         *      d.EmbeddedValueResolverAware  可以注入一个解析器，可以解析${}
         */
        /**
         * 2. 有同学说，b,c,d的功能用@Autowired就能实现，为啥还要用Aware接口呢？
         * 简单的说
         *      a.@Autowired 的解析需要bean后处理器，属于扩展功能
         *      b.而Aware接口属于内置功能，不加任何扩展，Spring就能识别
         *  某些情况下，扩展功能会失效，而内置功能不会
         */
        GenericApplicationContext context=new GenericApplicationContext();

        /**
         * 可以跑一下，注意一下执行的顺序
         */
//        context.registerBean("myBean",MyBean.class);
//        context.registerBean("myConfig1",MyConfig1.class);
        context.registerBean("myConfig2",MyConfig2.class);
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);//解析autowire
        context.registerBean(CommonAnnotationBeanPostProcessor.class);//解析@Resource  @PostConstruct  @PreDestroy
        /**
         * ConfigurationClassPostProcessor的作用，Baidu上面查的
         * 解析处理@PropertySource 注解
         * 解析@ComponentScan注解,扫描@Configuration、@Service、@Controller、@Repository和@Component注解并注册BeanDefinition
         * 解析@Import注解,然后进行实例化,并执行ImportBeanDefinitionRegistrar的registerBeanDefinitions逻辑,或者ImportSelector的selectImports逻辑
         * 解析@ImportResource注解,并加载相关配置信息
         * 解析方法级别@Bean注解并将返回值注册成BeanDefinition         *
         */
        context.registerBean(ConfigurationClassPostProcessor.class);//解析@Bean，@ComponentScan

        /**
         * ApplicationContextAware, InitializingBean这两个接口的作用是，使用@Autowired注解注入时，注入ApplicationContext可能会失效，但是用ApplicationContextAware不会失败，
         * 同时，用@PostConstructor进行初始化也会失效，但是用InitializingBean不会失败，这两个接口是spring的内置功能，不需要添加Bean的后置处理器
         */


        /**
         * 执行该方法时，也就是容器初始化时，执行顺序如下1，注册beanFactory后置处理器 2.注册bean后置处理器
         * 3.实例化单例(感觉3.3和3.4顺序有点问题，自己加的)
         *      3.1 创建和实例化
         *      3.2 依赖注入 例如@Autowired
         *      3.3 初始化扩展 例如@PostConstruct
         *      3.4 执行Aware和InitializingBean
         *      3.5初始化成功
         *
         * 但是如果初始化的配置类中有beanFactory后置处理器对象的话，执行顺序就改为
         * 1.实例化单例
         *      1.1 创建和初始化(这时由于还没有注册bean后置处理器，所以@Autowired之类的注解就失效了)
         *      1.2 执行Aware和InitializingBean
         * 2，注册beanFactory后置处理器 3.注册bean后置处理器
         * 这个时候  那些@Autowired之类的注解就失效了,因为有beanFactory后处理器，所以要先生成配置类，然后在注册里面的后处理器
         *
         * 所以Spring框架内部经常是用Aware和InitializingBean接口
         */
        context.refresh();

        Arrays.stream(context.getBeanDefinitionNames()).forEach(beanName->{
            log.info("======beanName:{}",beanName);
        });



        context.close();




    }



}
