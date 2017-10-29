package com.lonecpp.core.vo;

/**
 * @author seven sins
 * @date 2017年10月28日 上午1:25:21
 */
public class RequestParam {
	/**
	 * 请求接口
	 */
	private String command;
	/**
	 * 请求内容
	 */
	private Object content;
	/**
	 * 当前连接ID
	 */
	private long id;

	public RequestParam() {
		super();
	}
	
	public RequestParam(long id, String command, Object content) {
		super();
		this.command = command;
		this.content = content;
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

}
