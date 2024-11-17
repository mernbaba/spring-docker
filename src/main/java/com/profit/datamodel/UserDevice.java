package com.profit.datamodel;

import java.sql.Timestamp;
import java.time.LocalDate;

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
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "tb_user_device", uniqueConstraints = @UniqueConstraint(name = "uk_user_device_1", columnNames = { "id", "branch_code",
		"company_code" }))
@Data
public class UserDevice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_code", nullable = false, length = 56)
	private String userCode;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "user_type", nullable = false, length = 12)
	private String userType;
	
	@Column(name = "phone_number", nullable = false, length = 20)
	private String phoneNumber;
	
	@Column(name = "branch_code", nullable = false, length = 56)
	private String branchCode;

	@Column(name = "company_code", nullable = false, length = 56)
	private String companyCode;
	
	@Column(name = "old_device_id")
	private String oldDeviceId;
	
	@Column(name = "new_device_id")
	private String newDeviceId;
	
	@Column(name = "device_name")
	private String deviceName;
	
	@Column(name = "status", length = 32)
	private String status ;
	
	@Column(name = "created_by", nullable = false, length = 32)
	private String createdBy;

	@Column(name = "created_date", nullable = false)
	@CreationTimestamp
	private Timestamp createdDate;

	@Column(name = "last_modified_by", length = 32)
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	@UpdateTimestamp
	private Timestamp lastModifiedDate;
	
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "branch_code", referencedColumnName = "branch_code", insertable = false, updatable = false),
			@JoinColumn(name = "company_code", referencedColumnName = "company_code", insertable = false, updatable = false) })
	private BranchMaster branchMaster;

}
