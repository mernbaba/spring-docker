package com.profit.datamodel;

import java.io.Serializable;
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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "staff_master")
public class StaffMaster implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "staff_code", nullable = false, length = 56, unique = true)
	private String staffCode;

	@Column(name = "staff_name", nullable = false, length = 255)
	private String staffName;

	@Column(name = "gender", length = 10)
	private String gender;

	@Column(name = "staff_type", length = 50)
	private String staffType;
	
	@Column(name = "user_type", length = 50)
	private String userType;

	@Column(name = "phone", unique = true, nullable = false, length = 20)
	private String phone;

	@Column(name = "address", length = 500)
	private String address;

	@Column(name = "gmail", length = 100, unique = true)
	private String gmail;

	@Column(name = "shift", length = 50)
	private String shift;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	@Lob // Large Object for profile picture (binary data)
	@Column(name = "profile_pic")
	private byte[] profilePic;

	@Column(name = "is_pt", nullable = false)
	private Boolean isPT =  false;

	@Column(name = "branch_code", nullable = false)
	private String branchCode;

	@Column(name = "company_code", nullable = false)
	private String companyCode;

	@Column(name = "createdby", length = 32)
	private String createdBy;

	@Column(name = "created_date")
	@CreationTimestamp
	private LocalDate createdDate;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	@UpdateTimestamp
	private LocalDate lastModifiedDate;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "branch_code", referencedColumnName = "branch_code", insertable = false, updatable = false),
			@JoinColumn(name = "company_code", referencedColumnName = "company_code", insertable = false, updatable = false) })
	private BranchMaster branchMaster;
}
