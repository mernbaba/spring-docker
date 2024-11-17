package com.profit.datamodel;

import java.io.Serializable;
import java.sql.Timestamp;

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
@Table(name = "tb_role_mapping", uniqueConstraints = @UniqueConstraint(columnNames = { "user_code", "role_code",
		"branch_code", "company_code" }))
@Data
public class RoleMapping implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // Primary key

	@Column(name = "user_code", nullable = false, length = 56)
	private String userCode;

	@Column(name = "role_code", nullable = false, length = 56)
	private String roleCode;

	@Column(name = "branch_code", nullable = false, length = 56)
	private String branchCode;

	@Column(name = "company_code", nullable = false, length = 56)
	private String companyCode;

	@Column(name = "createdby", length = 32, nullable = false)
	private String createdBy;

	@Column(name = "created_date")
	@CreationTimestamp
	private Timestamp createdDate;
	
	@Column(name = "last_modified_by", length = 32)
	private String lastModifiedBy;
	
	@Column(name = "last_modified_date")
	@UpdateTimestamp
	private Timestamp lastModifiedDate;

	// Relationships
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "company_code", referencedColumnName = "company_code", insertable = false, updatable = false),
			@JoinColumn(name = "branch_code", referencedColumnName = "branch_code", insertable = false, updatable = false) })
	private BranchMaster branch; // Assuming you have a Branch entity

//	@ManyToOne
//	@JoinColumn(name = "user_code", referencedColumnName = "user_code", insertable = false, updatable = false)
//	private SecUser secUser;

	@ManyToOne
	@JoinColumn(name = "role_code", referencedColumnName = "role_code", insertable = false, updatable = false)
	private RoleMaster role;
}
