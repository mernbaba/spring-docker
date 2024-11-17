package com.profit.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;

@Data
public class CustomerMasterDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String customerCode;
	private String customerName;
	private String phoneNumber;
	private String email;
	private byte[] profilePic; // Consider using a different type if needed
	private String paymentPlan;
	private LocalDate startDateOfPlan;
	private LocalDate endDateOfPlan;
	private String address;
	private String workoutPlan;
	private String gender;
	private Boolean hasPT;
	private String staffCode;
	private String staffName;
	private String shift;
	private Float weight;
	private Boolean isActive;
	private String companyCode;
	private String branchCode;
	private String createdBy;
	private LocalDate createdDate;
	private String lastModifiedBy;
	private LocalDate lastModifiedDate;
	private String deviceId;
	private String status;
	private String excused;

}
