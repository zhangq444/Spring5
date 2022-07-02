package com.itheima.study.a40;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11Nio2Protocol;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Set;

/**
 * 这一讲是讲内嵌的Tomcat，这个可以结合a40包中的图片，讲了tomcat的结构，以及用编程的方式创建tomcat
 * 整个这个类，是用编程的方式启动了tomcat服务器，设置了协议和监听端口,最后用编程的方式，添加了一个servlet，并让它工作起来了
 *
 * @author grzha
 */
public class TestTomcat {

    public static void main(String[] args) throws Exception {
        // 1.创建Tomcat对象
        Tomcat tomcat = new Tomcat();
        // 设置基础目录,Tomcat在工作时可能要产生一些临时文件等，需要存放在基础目录下面，这里用的是相对路径
        tomcat.setBaseDir("tomcat");

        // 2.创建项目文件夹，即docBase文件夹,这个目录就是用来放项目的资源的，例如class文件等等
        File docBase = Files.createTempDirectory("boot.").toFile();
        //当程序退出时，删除临时目录
        docBase.deleteOnExit();

        // 3.创建Tomcat项目，在Tomcat中称为Context
        //第一个参数指定虚拟路径，如果要设为/，就传""，第二个参数就是docBase的实际路径
        Context context = tomcat.addContext("", docBase.getAbsolutePath());

        // 4.编程添加Servlet
        context.addServletContainerInitializer(new ServletContainerInitializer() {
            @Override
            public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
                HelloServlet helloServlet = new HelloServlet();
                servletContext.addServlet("MyServlet", helloServlet).addMapping("/hello");
            }
        }, Collections.emptySet());

        // 5.启动Tomcat
        tomcat.start();

        // 6.创建连接器，设置监听端口
        //Http11Nio2Protocol是指定协议
        Connector connector = new Connector(new Http11Nio2Protocol());
        //设置监听的端口
        connector.setPort(8080);
        tomcat.setConnector(connector);

    }


}
