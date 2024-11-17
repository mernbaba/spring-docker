package com.profit.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class MessagesDTO {

	private Long messageId;

	private Date messageDate;

	private String partyCode;

	private String partyType;

	private String messageType;

	private String otp;

	private BigDecimal amount;

	private String message;

	private String companyCode;

}
