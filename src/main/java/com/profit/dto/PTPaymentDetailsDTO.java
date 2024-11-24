package com.profit.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class PTPaymentDetailsDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private Long ptPaymentSummeryId;

	private String paymentMode;

	private BigDecimal amount;

	private String createdBy;

	private LocalDate createdDate;

	private String lastModifiedBy;

	private LocalDate lastModifiedDate;

}
