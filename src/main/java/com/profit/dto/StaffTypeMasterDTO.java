package com.profit.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class StaffTypeMasterDTO {

	private Long id;

	private String staffTypeCode;

	private String companyCode;

	private String branchCode;

	private String createdBy;

	private Timestamp createdDate;

}
