package com.lonecpp.server.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.lonecpp.core.common.Status;
import com.lonecpp.core.pb.RequestProbuf.PbRequest;
import com.lonecpp.core.pb.ResponseProbuf.PbResponse;
import com.lonecpp.core.pb.ResponseProbuf.PbResponse.Builder;
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
				return new Result(Status.ERROR, "获取目标控制器失败");
			}

			Object bean = beanMethod.getBean();
			Method method = beanMethod.getMethod();

			Class<?> paramType = method.getParameterTypes()[0];
			Object parameter = JSONObject.parseObject(JSONObject.toJSONString(request.getContent()), paramType);
			Result result = (Result) method.invoke(bean, parameter);
			result.setId(request.getId());
			
			return result;
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return null;
	}
	
	public static Object execute(PbRequest pbRequest) {
		PbResponse.Builder pbResponse = PbResponse.newBuilder();
		Object parameterObj = null;
		try {
			String command = pbRequest.getCommand();
			BeanMethod beanMethod = beanMap.get(command);
			if (beanMethod == null) {
				pbResponse.setStatus(Status.NOTFOUND);
				pbResponse.setMessage("获取目标控制器失败");
				return pbResponse;
			}
			//
			Method method = beanMethod.getMethod();
			Object bean = beanMethod.getBean();
			if(method.getParameterTypes() == null || method.getParameterTypes().length == 0) {
				//
			} else {
				String bool = "boolean";
				// 获取目标方法参数类型
				Class<?> parameterType = method.getParameterTypes()[0];
				// 目标方法参数类型的所有构造方法
				Constructor<?>[] constructors = parameterType.getDeclaredConstructors();
				//
				Constructor<?> constructor = null;
				for(Constructor<?> item: constructors) {
					if(bool.equals(item.getParameterTypes()[0].getName())) {
						constructor = item;
					}
				}
				if(constructor != null) {
					constructor.setAccessible(true);
				}
				// 初始化参数
				parameterObj = constructor.newInstance(true);
				ByteString requestBody = pbRequest.getBody();
				Method parameterMethod = parameterType.getMethod("parseFrom", ByteString.class);
				parameterObj = parameterMethod.invoke(parameterObj, requestBody);
			}
			
			Object obj = method.invoke(bean, parameterObj);
			pbResponse = (Builder) obj;
			pbResponse.setId(pbRequest.getId());
		} catch (Exception e) {
			LOGGER.error(e);
			pbResponse.setStatus(Status.ERROR);
			pbResponse.setMessage("请求出错");
		}
		return pbResponse;
	}
}
