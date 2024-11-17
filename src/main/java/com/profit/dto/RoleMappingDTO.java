package com.profit.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class RoleMappingDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id; // Primary key
	private String userCode;
	private String roleCode;
	private String branchCode;
	private String companyCode;
	private String createdBy;
	private Boolean isDelete;
	private Timestamp createdDate;
	private String lastModifiedBy;
	private Timestamp lastModifiedDate;

}
