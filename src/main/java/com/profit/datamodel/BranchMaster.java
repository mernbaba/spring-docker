package com.profit.datamodel;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "tb_branch_master", uniqueConstraints = @UniqueConstraint(columnNames = { "branch_code",
		"company_code" }))
@Data
public class BranchMaster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long branchId;

	@Column(name = "branch_code", nullable = false, length = 56, unique = true)
	private String branchCode;

	@Column(name = "branch_name", nullable = false)
	private String branchName;

	@Column(name = "short_name", length = 5)
	private String shortName;

	@Column(name = "priority")
	private Integer priority;

	@Column(name = "tin", length = 56)
	private String tin;

	@Column(name = "timezone", length = 56)
	private String timezone;

	@Column(name = "branch_lock", nullable = false)
	private Boolean branchLock;

	@Column(name = "company_code", nullable = false, length = 56)
	private String companyCode;

	@Column(name = "address", length = 500)
	private String address;

	@Column(name = "city", length = 255)
	private String city;

	@Column(name = "state", length = 255)
	private String state;

	@Column(name = "country", length = 255)
	private String country;

	@Column(name = "zip_code", length = 12)
	private String zipCode;

	@Column(name = "phone_no", length = 20)
	private String phoneNo;

	@Column(name = "file_dir", length = 255)
	private String fileDir;

	@Column(name = "day_closing_time")
	private String dayClosingTime;

	@Column(name = "is_headoffice", nullable = false)
	private Boolean isHeadOffice;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	@Column(name = "created_by", nullable = false, length = 12)
	private String createdBy;

	@Column(name = "created_date", nullable = false)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "last_modified_by", length = 12)
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	@UpdateTimestamp
	private Date lastModifiedDate;

	@Column(name = "stamp_file_dir", length = 255)
	private String stampFileDir;

	@Column(name = "country_code", length = 255)
	private String countryCode;

	@ManyToOne
	@JoinColumn(name = "company_code", referencedColumnName = "company_code", insertable = false, updatable = false)
	private CompanyMaster companyMaster;

//	@ManyToOne
//	@JoinColumn(name = "created_by", referencedColumnName = "user_name", insertable = false, updatable = false)
//	private SecUser secUser;
	

}
