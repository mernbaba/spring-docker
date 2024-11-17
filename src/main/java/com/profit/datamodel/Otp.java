package com.profit.datamodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "tb_otp")
@Entity
public class Otp implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "party_code", nullable = false)
	private String partyCode;

	@Column(name = "party_type", nullable = false)
	private String partyType;

	@Column(name = "activity", nullable = false)
	private String activity;

	@Column(name = "activity_id", nullable = false)
	private Long activityId;

	@Column(name = "otp", nullable = false)
	private String otp;

	@Column(name = "otp_gentime", nullable = false)
	private Date otpGentime;

	@Column(name = "otp_exptime", nullable = false)
	private Date otpExptime;

	@Column(name = "otp_status")
	private String otpStatus;

	@Column(name = "app_name")
	private String appName;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "company_code")
	private String companyCode;

	@Column(name = "qty")
	private Integer qty;

	@Column(name = "unsuccessful_attempts")
	private Integer unsuccessfulAttempts;

	@Column(name = "createdby", nullable = false)
	private String createdby;

	@Column(name = "createddate", nullable = false)
	private Date createddate;

	@Column(name = "lastmodifiedby")
	private String lastmodifiedby;

	@Column(name = "lastmodifieddate")
	private Date lastmodifieddate;

	@ManyToOne
	@JoinColumn(name = "createdby", referencedColumnName = "user_name", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_user_name"))
	private SecUser secUser;
}
