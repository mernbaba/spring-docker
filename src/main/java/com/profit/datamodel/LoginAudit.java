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
import jakarta.persistence.JoinColumns;
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

	@Column(name = "user_name", nullable = false, length = 50)
	private String userName;

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
	
	@Column(name = "app_name", length = 20)
	private String appName;

	@Column(name = "comments", columnDefinition = "TEXT")
	private String comments; 

	@Column(name = "company_code",  length = 56)
	private String companyCode;
	
	@Column(name = "branch_code", length = 56)
	private String branchCode;
	
	
	@ManyToOne
	@JoinColumn(name = "user_name", referencedColumnName = "user_name", insertable = false, updatable = false)
	private SecUser secUser;

}