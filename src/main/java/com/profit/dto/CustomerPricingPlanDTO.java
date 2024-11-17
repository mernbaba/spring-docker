package com.profit.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class CustomerPricingPlanDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer planCode;
	private String planName;
	private String description;
	private Double amount;
	private Boolean isActive;
	private String branchCode;
	private String companyCode;
	private String createdBy;
	private Timestamp createdDate;
	private String lastModifiedBy;
	private Timestamp lastModifiedDate;

}
