package com.itheima.study.a28;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 这一讲讲了MessageConverter的作用，是将返回的对象和消息做了一个转换，将返回对象转换为某一种格式的消息，json或xml
 * @ResponseBody注解是RequestResponseBodyMethodProcessor解析的，但是消息的转换工作是MappingJackson2HttpMessageConverter来完成的
 * @author grzha
 */
public class A28 {

    public static void main(String[] args) throws IOException, HttpMediaTypeNotAcceptableException, NoSuchMethodException {
        test1();
        test2();
        test3();
        test4();


    }


    @ResponseBody
    public User user(){
        return null;
    }

    /**
     * 讲解有多个消息转换器的时候的情况，如果没有特殊指定（在请求头和响应中指定），是根据RequestResponseBodyMethodProcessor里面添加消息转换器的顺序决定的
     *
     * @throws NoSuchMethodException
     * @throws HttpMediaTypeNotAcceptableException
     * @throws IOException
     */
    public static void test4() throws NoSuchMethodException, HttpMediaTypeNotAcceptableException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);

        //如果请求头中加了Accept头，指定接收类型为xml，那么就会转换为xml格式的字符串
        request.addHeader("Accept","application/xml");
        //在响应中设置contentType，这个优先级比请求头要高
        response.setContentType("application/json");

        RequestResponseBodyMethodProcessor processor = new RequestResponseBodyMethodProcessor(Lists.newArrayList(new MappingJackson2HttpMessageConverter(),new MappingJackson2XmlHttpMessageConverter()));
        processor.handleReturnValue(User.builder().name("张三").age(18).build(),new MethodParameter(A28.class.getMethod("user"),-1),new ModelAndViewContainer(),webRequest);
        System.out.println(new String(response.getContentAsByteArray(),StandardCharsets.UTF_8));
    }


    /**
     * 将json字符串转为对象
     * @throws IOException
     */
    public static void test3() throws IOException {
        MockHttpInputMessage message = new MockHttpInputMessage("""
                {
                    "name":"李四",
                    "age":20
                }
                """.getBytes(StandardCharsets.UTF_8));
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if(converter.canRead(User.class,MediaType.APPLICATION_JSON)){
            Object read = converter.read(User.class, message);
            System.out.println(read);
        }

    }

    /**
     * 将对象转为xml格式字符串
     * @throws IOException
     */
    public static void test2() throws IOException {
        //要写出的消息MockHttpOutputMessage
        MockHttpOutputMessage message = new MockHttpOutputMessage();
        //转换器，将对象转换为json字符串
        MappingJackson2XmlHttpMessageConverter converter = new MappingJackson2XmlHttpMessageConverter();
        if(converter.canWrite(User.class, MediaType.APPLICATION_XML)){
            converter.write(User.builder().name("张三").age(18).build(),MediaType.APPLICATION_XML,message);
            System.out.println(message.getBodyAsString());
        }
    }

    /**
     * 将对象转为json字符串
     * @throws IOException
     */
    public static void test1() throws IOException {
        //要写出的消息MockHttpOutputMessage
        MockHttpOutputMessage message = new MockHttpOutputMessage();
        //转换器，将对象转换为json字符串
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if(converter.canWrite(User.class, MediaType.APPLICATION_JSON)){
            converter.write(User.builder().name("张三").age(18).build(),MediaType.APPLICATION_JSON,message);
            System.out.println(message.getBodyAsString());
        }
    }




    @Data
    @Builder
    @ToString
    public static class User{
        private String name;
        private Integer age;

        @JsonCreator
        public User(@JsonProperty("name") String name, @JsonProperty("age") Integer age){
            this.name=name;
            this.age=age;
        }

    }




}
