package com.profit.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class CustomerPaymentDetailsDTO {
	
	private Long id;

	private Long paymentSummeryId;

	private String paymentMode;

	private BigDecimal amount;

	private String createdBy;

	private LocalDate createdDate;

	private String lastModifiedBy;

	private LocalDate lastModifiedDate;


}
