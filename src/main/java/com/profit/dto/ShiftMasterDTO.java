package com.profit.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class ShiftMasterDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String shiftCode;
	private String shiftName;
	private Boolean isActive;
	private String branchCode;
	private String companyCode;
	private String createdBy;
	private Timestamp createdDate;
	private String lastModifiedBy;
	private Timestamp lastModifiedDate;
}
