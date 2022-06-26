package com.itheima.study.a36;

/**
 *  当浏览器发送一个请求 http://localhost:8080/hello 后，请求到达服务器，其处理流程是：
 *  1.服务器提供了DispatcherServlet，它使用的是标准的Servlet技术
 *      路径：默认映射路径为/ ，即会匹配到所有请求URL，可作为请求的统一入口，也被称之为前端控制器
 *          例外：jsp不会匹配到DispatcherServlet
 *      创建：在Springboot中，由DispatcherServletAutoConfiguration这个自动配置类提供DispatcherServlet 的bean
 *      初始化：DispatcherServlet初始化时会优先到容器里面寻找各种组件，作为它的成员变量
 *          HandlerMapping：初始化时记录映射关系
 *          HandlerAdapter：初始化时准备参数解析器、返回值处理器、消息转换器
 *          HandlerExceptionResolver:初始化时准备参数解析器、返回值处理器、消息转换器（处理器异常解析器，用来处理控制器出现异常时的处理逻辑）
 *          ViewResolver:(这个老师没有讲，视图解析器)
 *  2.DispatcherServlet会利用RequestMappingHandlerMapping查找控制器方法
 *      例如根据/hello路径找到@RequestMapping("/hello")对应的控制器方法
 *      控制器方法会被封装为HandlerMethod对象，并结合匹配到的拦截器一起返回给DispatcherServlet
 *      HandlerMethod和拦截器合在一起成为HandlerExecutionChain(调用链)对象
 *  3.DispatcherServlet接下来会：
 *      1.调用拦截器的preHandle方法
 *      2.RequestMappingHandlerAdapter调用handle方法，准备数据绑定工厂、模型工厂、将HandlerMethod完善为ServletInvocableHandlerMethod
 *          @ControllerAdvice增强1：补充模型数据
 *          @ControllerAdvice增强2：补充自定义类型转换器
 *          使用HandlerMethodArgumentResolver准备参数
 *          @ControllerAdvice增强3：RequestBody增强
 *          调用ServletInvocableHandlerMethod(是在这一步里面反射调用Controller中的方法的)
 *          使用HandlerMethodReturnValueHandler处理返回值
 *              如果返回的ModelAndView为null，不走第4步视图解析及渲染流程
 *                  例如，标注了@ResponseBody的控制器方法，调用HttpMessageConverter来讲结果转换为JSON，这时返回的ModelAndView就为null
 *              如果返回的ModelAndView不为null，会在第4步走视图解析及渲染流程
 *              @ControllerAdvice增强4：ResponseBody增强
 *      3.调用拦截器的postHandle方法
 *      4.处理异常或视图渲染
 *          如果1~3出现异常，走ExceptionHandlerExceptionResolver处理异常流程
 *              @ControllerAdvice增强5：@ExceptionHandler增强
 *          正常，走视图解析及渲染流程
 *      5.调用拦截器的afterCompletion方法
 *
 *
 *
 *
 * @author grzha
 */
public class A36 {
//当浏览器发送一个请求 http://localhost:8080/hello 后，请求到达服务器，其处理流程是：
//   1.服务器提供了DispatcherServlet，它使用的是标准的Servlet技术
//       路径：默认映射路径为/ ，即会匹配到所有请求URL，可作为请求的统一入口，也被称之为前端控制器
//           例外：jsp不会匹配到DispatcherServlet
//       创建：在Springboot中，由DispatcherServletAutoConfiguration这个自动配置类提供DispatcherServlet 的bean
//       初始化：DispatcherServlet初始化时会优先到容器里面寻找各种组件，作为它的成员变量
//           HandlerMapping：初始化时记录映射关系
//           HandlerAdapter：初始化时准备参数解析器、返回值处理器、消息转换器
//           HandlerExceptionResolver:初始化时准备参数解析器、返回值处理器、消息转换器（处理器异常解析器，用来处理控制器出现异常时的处理逻辑）
//           ViewResolver:(这个老师没有讲，视图解析器)
//   2.DispatcherServlet会利用RequestMappingHandlerMapping查找控制器方法
//       例如根据/hello路径找到@RequestMapping("/hello")对应的控制器方法
//       控制器方法会被封装为HandlerMethod对象，并结合匹配到的拦截器一起返回给DispatcherServlet
//       HandlerMethod和拦截器合在一起成为HandlerExecutionChain(调用链)对象
//   3.DispatcherServlet接下来会：
//       1.调用拦截器的preHandle方法
//       2.RequestMappingHandlerAdapter调用handle方法，准备数据绑定工厂、模型工厂、将HandlerMethod完善为ServletInvocableHandlerMethod
//           @ControllerAdvice增强1：补充模型数据
//           @ControllerAdvice增强2：补充自定义类型转换器
//           使用HandlerMethodArgumentResolver准备参数
//           @ControllerAdvice增强3：RequestBody增强
//           调用ServletInvocableHandlerMethod(是在这一步里面反射调用Controller中的方法的)
//           使用HandlerMethodReturnValueHandler处理返回值
//               如果返回的ModelAndView为null，不走第4步视图解析及渲染流程
//                   例如，标注了@ResponseBody的控制器方法，调用HttpMessageConverter来讲结果转换为JSON，这时返回的ModelAndView就为null
//               如果返回的ModelAndView不为null，会在第4步走视图解析及渲染流程
//               @ControllerAdvice增强4：ResponseBody增强
//       3.调用拦截器的postHandle方法
//       4.处理异常或视图渲染
//           如果1~3出现异常，走ExceptionHandlerExceptionResolver处理异常流程
//               @ControllerAdvice增强5：@ExceptionHandler增强
//           正常，走视图解析及渲染流程
//       5.调用拦截器的afterCompletion方法
}
