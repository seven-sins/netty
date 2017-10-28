package com.lonecpp.core.vo;

/**
 * @author seven sins
 * @date 2017年10月28日 下午2:31:35
 */
public class Result {

	/**
	 * 响应代码
	 */
	private Integer code;
	/**
	 * 响应消息
	 */
	private String message;
	/**
	 * 响应数据
	 */
	private Object data;

	public Result() {
		super();
	}

	public Result(Integer code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	public Result(Integer code, Object data) {
		super();
		this.code = code;
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
