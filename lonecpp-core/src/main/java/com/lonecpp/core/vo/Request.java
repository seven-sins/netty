package com.lonecpp.core.vo;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author seven sins
 * @date 2017年10月28日 上午1:25:27
 */
public class Request {
	
	private String command;

	private Object content;

	private final long id;

	public static final AtomicLong NID = new AtomicLong(0);

	public Request() {
		id = NID.incrementAndGet();
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
