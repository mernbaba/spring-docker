package com.profit.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class PTPricingPlanDTO implements Serializable {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String staffCode;

	private String staffName;

	private Integer planCode;

	private String planName;

	private String description;

	private BigDecimal amount;

	private Boolean isActive;

	private String companyCode;

	private String branchCode;

	private String createdBy;

	private LocalDate createdDate;

	private String lastModifiedBy;

	private LocalDate lastModifiedDate;

}
