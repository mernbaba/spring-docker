package com.profit.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class UserDeviceDTO {

	private Long id;
	private String userCode;
	private String name;
	private String userType;
	private String phoneNumber;
	private String branchCode;
	private String companyCode;
	private String oldDeviceId;
	private String newDeviceId;
	private String deviceName;
	private String status ;
	private String createdBy;
	private Timestamp createdDate;
	private String lastModifiedBy;
	private Timestamp lastModifiedDate;

}
