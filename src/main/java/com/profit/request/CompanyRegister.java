package com.profit.request;

import lombok.Data;

@Data
public class CompanyRegister {
	
	private String companyName;
	private String branchName;
	private Boolean hasMultiBranch;
	private String email;
	private String phoneNumber;
//	private String userName;
	private String password;
	private String confirmPswd;
	private String staffName;
	private String branchSrtCode;
}
