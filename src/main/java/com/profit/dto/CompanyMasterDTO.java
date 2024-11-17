package com.profit.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CompanyMasterDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long companyId;

	private String staffName;

	private String phoneNo;

	private String userName;

	private String password;

	private String confirmPswd;

	private String email;

	private Boolean hasMultiBranch;

	private String companyName;

	private String companyCode;
	
	private String branchName;

}
