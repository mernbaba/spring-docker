package com.profit.datamodel;

import java.io.Serializable;

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
@Table(name = "user_type")
@Data
public class UserType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "usertype_code", nullable = false, length = 56, unique = true)
	private String userTypeCode;

	@Column(name = "usertype_name", nullable = false, length = 255)
	private String userTypeName;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;

	@Column(name = "branch_code", length = 56)
	private String branchCode;

	@Column(name = "company_code", nullable = false, length = 56)
	private String companyCode;

	// Many-to-One relationship with Company
//	@ManyToOne
//	@JoinColumn(name = "company_code", referencedColumnName = "company_code", insertable = false, updatable = false)
//	private CompanyMaster companyMaster;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "company_code", referencedColumnName = "company_code", insertable = false, updatable = false),
			@JoinColumn(name = "branch_code", referencedColumnName = "branch_code", insertable = false, updatable = false) })
	private BranchMaster branchMaster;
}
