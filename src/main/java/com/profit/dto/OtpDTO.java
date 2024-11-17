package com.profit.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class OtpDTO {

	private Long id;

	private String partyCode;

	private String partyType;

	private String activity;

	private Long activityId;

	private String otp;

	private Date otpGentime;

	private Date otpExptime;

	private String otpStatus;

	private String appName;

	private BigDecimal amount;

	private String companyCode;

	private Integer qty;

	private Integer unsuccessfulAttempts;

	private String createdby;

	private Date createddate;

	private String lastmodifiedby;

	private Date lastmodifieddate;

}
