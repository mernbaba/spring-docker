package com.profit.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;

@Data
public class StaffMasterDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String staffCode;
	private String staffName;
	private String gender;
	private String staffType;
	private String userType;
	private String phone;
	private String address;
	private String gmail;
	private String shift;
	private Boolean isActive;
	private byte[] profilePic;
	private Boolean isPT;
	private String branchCode;
	private String companyCode;
	private String deviceId;
	private String status;
	private String excused;
	private String createdBy;
	private LocalDate createdDate;
	private String lastModifiedBy;
	private LocalDate lastModifiedDate;

}
