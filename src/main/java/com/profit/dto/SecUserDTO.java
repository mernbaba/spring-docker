package com.profit.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SecUserDTO {

	private Long id;

	private String userName; //

	private String password; //

	private String userType; //

	private String userCode; // ""

	private String email; //

	private String branchCode; //

	private String companyCode; //

	private Boolean multiBranch = true; //

	private Boolean accountLocked = true; //

	private Boolean passwordExpired = true; //

	private Boolean firstLogin = true; //

	private Date lastWorkingDate;

	private String verificationCode;

	private Boolean isAdmin = false; //

	private Boolean isActive; //
	
	private String excused;

	private String createdBy; // ""

	private Date createdDate;

	private String lastModifiedBy;

	private Date lastModifiedDate;

	private String deviceId; //

	private String phoneNumber; //

}
