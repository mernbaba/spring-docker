package com.profit.request;

import lombok.Data;

@Data
public class ChangeDeviceRequest {
	
	private String userCode;
	private String name;
	private String gender;
	private String email;
	private String staffType;
	private String phoneNumber;
	private String shift;
	private String address;
	private String isActive;
	private String newDeviceId;
	private String deviceName;
	private String companyCode;
	private String branchCode;

}
