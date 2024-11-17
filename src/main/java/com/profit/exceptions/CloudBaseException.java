package com.profit.exceptions;

import com.profit.enumeration.ResponseCode;

public class CloudBaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private int code;
	private String message;
	private final String userMessage;
	private final Object object;

	public CloudBaseException(int code, String message, String userMessage) {
		super();
		this.code = code;
		this.message = message;
		this.userMessage = userMessage;
		this.object = null;
	}

	public CloudBaseException(ResponseCode responseStatus, Object object) {
		super();
		this.code = responseStatus.getCode();
		this.message = responseStatus.getMessage();
		this.userMessage = responseStatus.getUserMessage();
		this.object = object;
	}

	public CloudBaseException(ResponseCode responseStatus) {
		super();
		this.code = responseStatus.getCode();
		this.message = responseStatus.getMessage();
		this.userMessage = responseStatus.getUserMessage();
		this.object = null;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public Object getObject() {
		return object;
	}
	
	 @Override
	    public synchronized Throwable fillInStackTrace() {
	        return this;
	    }

	@Override
	public String toString() {
		return "CloudBaseException [code=" + code + ", message=" + message + ", userMessage=" + userMessage
				+ ", object=" + object + "]";
	}
	 
	 
	 

//	public CloudBaseException(String message) {
//		super();
//		this.message = message;
//		// TODO Auto-generated constructor stub
//	}
}