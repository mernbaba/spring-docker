package com.profit.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class CustomerPaymentSummaryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String customerCode;
	private String customerName;
	private BigDecimal actualAmount;
	private BigDecimal amountPaid;
	private BigDecimal discountAmount;
	private BigDecimal balanceAmount;
	private Boolean settled;
	private LocalDate planStartDate;
	private LocalDate planEndDate;
	private String paymentPlan;
	private String branchCode;
	private String companyCode;
	private String createdBy;
	private LocalDate createdDate;
	private String lastModifiedBy;
	private LocalDate lastModifiedDate;
	private List<CustomerPaymentDetailsDTO> customerPaymentDetails;

}
