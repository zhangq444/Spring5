package com.ithema.study.a05;

import com.alibaba.fastjson2.JSON;
import com.ithema.study.a05.component.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 *  这一讲主要讲BeanFactory的后处理器
 * @author grzha
 */
@Slf4j
public class A05Application {

    public static void main(String[] args) throws IOException {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);
        //beanFactory后置处理器，用来解析@ComponentScan  @Bean @Import @ImportResource
        //context.registerBean(ConfigurationClassPostProcessor.class);
        //解析@Mapper注解，增加的同时需要指定要扫描的包路径
        /*context.registerBean(MapperScannerConfigurer.class, new BeanDefinitionCustomizer() {
            @Override
            public void customize(BeanDefinition beanDefinition) {
                beanDefinition.getPropertyValues().add("basePackage","com.ithema.study.a05.mapper");
            }
        });*/
        /*context.registerBean(MapperScannerConfigurer.class,beanDefinition -> {
            beanDefinition.getPropertyValues().add("basePackage","com.ithema.study.a05.mapper");
        });*/

        //自己实现ConfigurationClassPostProcessor功能，自己解析@Component注解生成bean
//        ComponentScan annotation = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
//        if (Objects.nonNull(annotation)) {
//            Arrays.stream(annotation.basePackages()).forEach(basePackage -> {
//                log.info("======{}", JSON.toJSONString(basePackage));
//                String path = "classpath*:" + basePackage.replace(".", "/") + "/**/*.class";
//                log.info("======{}", JSON.toJSONString(path));
//                try {
//                    CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
//                    Resource[] resources = context.getResources(path);
//                    DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
//                    AnnotationBeanNameGenerator generator=new AnnotationBeanNameGenerator();
//                    for (Resource resource : resources) {
//                        MetadataReader reader = factory.getMetadataReader(resource);
//                        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
//                        log.info("======类名:{}", reader.getClassMetadata().getClassName());
//                        log.info("======类上是否加了@Component: {}", annotationMetadata.hasAnnotation(Component.class.getName()));
//                        log.info("======类上是否加了@Component 派生注解: {}", annotationMetadata.hasMetaAnnotation(Component.class.getName()));
//
//                        if (annotationMetadata.hasAnnotation(Component.class.getName()) || annotationMetadata.hasMetaAnnotation(Component.class.getName())) {
//                            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(reader.getClassMetadata().getClassName()).getBeanDefinition();
//                            String name = generator.generateBeanName(beanDefinition, beanFactory);
//                            beanFactory.registerBeanDefinition(name,beanDefinition);
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            });
//        }
        //自己实现@ComponentScan注解生成Bean
        context.registerBean(ComponentScanPostProcessor.class);

        //自己实现@Bean注解的解析
//        CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
//        MetadataReader reader = factory.getMetadataReader(new ClassPathResource("com/ithema/study/a05/Config.class"));
//        Set<MethodMetadata> methods = reader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
//        methods.stream().forEach(methodMetadata -> log.info("======method:{}",methodMetadata));
//        for (MethodMetadata method : methods) {
//            //获取@Bean注解中的属性值，属性的key为initMethod
//            String initMethod = method.getAnnotationAttributes(Bean.class.getName()).get("initMethod").toString();
//
//            BeanDefinitionBuilder builder=BeanDefinitionBuilder.genericBeanDefinition();
//            //设置自动装配，就是在config类中的工厂方法，如果方法有参数，那些参数自动装配
//            builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
//            if(StringUtils.isNotBlank(initMethod)){
//                //设置初始化方法，传入方法名
//                builder.setInitMethodName(initMethod);
//            }
//            AbstractBeanDefinition beanDefinition = builder.setFactoryMethodOnBean(method.getMethodName(), "config").getBeanDefinition();
//            context.getDefaultListableBeanFactory().registerBeanDefinition(method.getMethodName(),beanDefinition);
//        }
        //自己实现@Bean注解的解析
        context.registerBean(AtBeanPostProcessor.class);
        //自己实现@Mapper注解的解析
        context.registerBean(MapperPostProcessor.class);


        context.refresh();

        /*AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(Config.class);
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(beanName->{
            log.info("======springBean:{}",beanName);
        });*/
        /*Arrays.stream(SpringBeanUtil.getApplicationContext().getBeanDefinitionNames()).forEach(beanName->{
            log.info("======springBean:{}",beanName);
        });*/
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Arrays.stream(context.getBeanDefinitionNames()).forEach(beanName->log.info("======beanName：{}",beanName));

        context.close();


    }


}
