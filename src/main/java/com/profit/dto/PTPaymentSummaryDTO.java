package com.profit.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class PTPaymentSummaryDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String customerCode;

	private String customerName;
	
	private String staffCode;

	private String staffName;

	private BigDecimal actualAmount;

	private BigDecimal amountPaid;

	private BigDecimal discountAmount;

	private BigDecimal balanceAmount;

	private Boolean settled;

	private LocalDate ptStartDateOfPlan;
	
	private LocalDate ptEndDateOfPlan;
	
	private String ptPaymentPlan;
	
	private String branchCode;

	private String companyCode;
	
	private Boolean isActive;

	private String createdBy;

	private LocalDate createdDate;

	private String lastModifiedBy;

	private LocalDate lastModifiedDate;
	
	private List<PTPaymentDetailsDTO> ptPaymentDetails;

}
