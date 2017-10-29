package com.lonecpp.server.config;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.lonecpp.server.annotation.Action;

/**
 * 初始扫描并存储controller
 * @author seven sins
 * @date 2017年10月28日 上午1:11:58
 */
@Component
public class InitMedia implements ApplicationListener<ContextRefreshedEvent>, Ordered {

	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(Controller.class);
		for(String key: beans.keySet()){
			Object bean = beans.get(key);
			Method[] methods = bean.getClass().getDeclaredMethods();
			for(Method method: methods){
				if(method.isAnnotationPresent(Action.class)){
					Action action = method.getAnnotation(Action.class);
					String command = action.value();
					BeanMethod beanMethod = new BeanMethod();
					beanMethod.setBean(bean);
					beanMethod.setMethod(method);
					Media.beanMap.put(command, beanMethod);
				}
			}
		}
	}

}
