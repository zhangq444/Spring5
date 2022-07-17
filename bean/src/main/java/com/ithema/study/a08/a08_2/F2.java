package com.ithema.study.a08.a08_2;

import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * @author grzha
 */
/**
 * proxyMode = ScopedProxyMode.TARGET_CLASS这个方法也可以解决scope失效，原理也是生成代理
 */
@Scope(value = "prototype",proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
@ToString(callSuper = true)
public class F2 {



}
