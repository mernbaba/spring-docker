package com.profit.datamodel;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "sec_user")
@Data
public class SecUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_name", nullable = false, length = 32, unique = true)
	private String userName;

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "user_type", nullable = false, length = 12)
	private String userType;

	@Column(name = "user_code", unique = true, nullable = false, length = 56)
	private String userCode;

	@Column(name = "email", length = 100, unique = true, nullable = false)
	private String email;

	@Column(name = "branch_code", length = 56)
	private String branchCode;

	@Column(name = "company_code", nullable = false, length = 56)
	private String companyCode;

	@Column(name = "multi_branch", nullable = false)
	private Boolean multiBranch = true;

	@Column(name = "account_locked", nullable = false)
	private Boolean accountLocked = true;

	@Column(name = "password_expired", nullable = false)
	private Boolean passwordExpired = true;

	@Column(name = "first_login")
	private Boolean firstLogin = true;

	@Column(name = "last_working_date")
	private Date lastWorkingDate;

	@Column(name = "verification_code", length = 56)
	private String verificationCode;

	@Column(name = "is_admin")
	private Boolean isAdmin = false;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
	
	@Column(name = "excused", length = 100)
	private String excused;

	@Column(name = "created_by", nullable = false, length = 32)
	private String createdBy;

	@Column(name = "created_date", nullable = false)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "last_modified_by", length = 32)
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	@UpdateTimestamp
	private Date lastModifiedDate;

	@Column(name = "device_id")
	private String deviceId;

	@Column(name = "phone_number", unique = true, nullable = false, length = 20)
	private String phoneNumber;

//	// Relationships
//	@ManyToOne
//	@JoinColumn(name = "user_type", referencedColumnName = "usertype_code", insertable = false, updatable = false)
//	private UserType userTypeEntity;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "branch_code", referencedColumnName = "branch_code", insertable = false, updatable = false),
			@JoinColumn(name = "company_code", referencedColumnName = "company_code", insertable = false, updatable = false) })
	private BranchMaster branchMaster;

}
