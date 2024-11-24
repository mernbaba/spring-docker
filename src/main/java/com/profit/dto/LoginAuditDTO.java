package com.profit.dto;

import java.io.Serializable;
import java.sql.Date;

import lombok.Data;

@Data
public class LoginAuditDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String userName;

	private Date loginDateTime;

	private String deviceId;

	private String ip;

	private String fbToken;

	private String status;
	
	private String appName;

	private String comments; 

	private String companyCode;
	
	private String branchCode;

}
