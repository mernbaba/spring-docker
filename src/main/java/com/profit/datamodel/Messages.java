package com.profit.datamodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_messages")
public class Messages implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id", nullable = false)
	private Long messageId;

	@Column(name = "message_date")
	private Date messageDate;

	@Column(name = "party_code")
	private String partyCode;

	@Column(name = "party_type")
	private String partyType;

	@Column(name = "message_type")
	private String messageType;

	@Column(name = "otp")
	private String otp;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "message")
	private String message;

	@Column(name = "company_code")
	private String companyCode;
}
