package com.lonecpp.server.config;

import java.lang.reflect.Method;

/**
 * @author seven sins
 * @date 2017年10月28日 上午1:12:14
 */
public class BeanMethod {
	
	private Object bean;
	
	private Method method;

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
}
