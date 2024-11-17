package com.profit.dto;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.profit.enumeration.ResponseCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseStatus {


    private int code;

    private String message;
    
    private String userMessage;

    private Long timestamp = new Date().getTime();

    private Map<String, String> errors;

    public ResponseStatus(int code, String message,String userMessage) {
        super();
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
    }
    public ResponseStatus(ResponseCode statusCode) {
        this(statusCode.getCode(), statusCode.getMessage(),statusCode.getUserMessage());
    }

    public ResponseStatus(ResponseCode responseCode, Map<String, String> errors) {
        this(responseCode.getCode(), responseCode.getMessage(),responseCode.getUserMessage());
        this.errors = errors;
    }
    
  

    public ResponseStatus(Map<String, String> errors) {
		
		this.message = errors.get("message");
		
	}
	public ResponseStatus() {}

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
    

    public String getUserMessage() {
		return userMessage;
	}
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
	@Override
	public String toString() {
		return "ResponseStatus [code=" + code + ", message=" + message + ", userMessage=" + userMessage + ", timestamp="
				+ timestamp + ", errors=" + errors + "]";
	}
	



}
