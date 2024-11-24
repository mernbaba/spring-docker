package com.profit.request;

import java.io.Serializable;

import lombok.Data;

@Data
public class JwtRequest implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;

	private String username;
	private String password;
	private String app;
	private String branch;
	private String company;
	private String version;
	private String mac;
	private String uid;
	private String fbToken;
	private String comments;
	

}