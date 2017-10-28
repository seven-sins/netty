package com.lonecpp.server.config;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.lonecpp.core.vo.RequestParam;
import com.lonecpp.core.vo.Result;

/**
 * @author seven sins
 * @date 2017年10月28日 上午1:12:20
 */
public class Media {
	static final Logger LOGGER = Logger.getLogger(Media.class);
	public static Map<String, BeanMethod> beanMap;

	static {
		beanMap = new HashMap<String, BeanMethod>();
	}

	public static Result execute(RequestParam request) {
		try {
			String command = request.getCommand();
			BeanMethod beanMethod = beanMap.get(command);
			if (beanMethod == null) {
				return new Result(400, "获取目标控制器失败");
			}

			Object bean = beanMethod.getBean();
			Method method = beanMethod.getMethod();

			Class<?> paramType = method.getParameterTypes()[0];
			Object parameter = JSONObject.parseObject(JSONObject.toJSONString(request.getContent()), paramType);
			Result result = (Result) method.invoke(bean, parameter);
			
			return result;
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return null;

	}
}
