package com.lonecpp.core.vo;

/**
 * @author seven sins
 * @date 2017年10月28日 下午2:31:35
 */
public class Result {
	/**
	 * 请求ID
	 */
	private Long id;
	/**
	 * 响应状态
	 */
	private Integer status;
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

	public Result(Integer status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	public Result(Integer status, Object data) {
		super();
		this.status = status;
		this.data = data;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
