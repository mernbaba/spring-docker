package com.profit.datamodel;

import java.io.Serializable;
import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_login_audit")
@Data
public class LoginAudit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", nullable = false, length = 50)
	private String username;

	@Column(name = "login_date_time", nullable = false)
	@CreationTimestamp
	private Date loginDateTime;

	@Column(name = "device_id", length = 100)
	private String deviceId;

	@Column(name = "ip", length = 45)
	private String ip;

	@Column(name = "fb_token", length = 255)
	private String fbToken;

	@Column(name = "status")
	private String status;

	@Column(name = "comments", length = 255)
	private String comments; // Comments if the login fails

	@Column(name = "company_code", nullable = false, length = 56)
	private String companyCode;

	@ManyToOne
	@JoinColumn(name = "company_code", referencedColumnName = "company_code", insertable = false, updatable = false)
	private CompanyMaster companyMaster;
}