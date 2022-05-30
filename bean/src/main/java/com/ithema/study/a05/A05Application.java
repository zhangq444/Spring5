package com.ithema.study.a05;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class A05Application {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);
        //beanFactory后置处理器，用来解析@ComponentScan  @Bean @Import @ImportResource
        //context.registerBean(ConfigurationClassPostProcessor.class);
        //解析@Mapper注解
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
        context.registerBean(ComponentScanPostProcessor.class);

        context.refresh();

        Arrays.stream(context.getBeanDefinitionNames()).forEach(beanName->log.info("======{}",beanName));

        context.close();


    }


}
