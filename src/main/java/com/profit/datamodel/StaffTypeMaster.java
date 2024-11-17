package com.profit.datamodel;

import java.sql.Timestamp;

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
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Table(name = "tb_staff_type_master", uniqueConstraints = @UniqueConstraint(name = "unique_staff_type", columnNames = {
		"staff_type_code", "branch_code", "company_code" }))
@Entity
public class StaffTypeMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "staff_type_code", nullable = false)
	private String staffTypeCode;

	@Column(name = "company_code", length = 56, nullable = false)
	private String companyCode;

	@Column(name = "branch_code", length = 56, nullable = false)
	private String branchCode;

	@Column(name = "created_by", nullable = false)
	private String createdBy;

	@Column(name = "created_date")
	@CreationTimestamp
	private Timestamp createdDate;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "branch_code", referencedColumnName = "branch_code", updatable = false, insertable = false),
			@JoinColumn(name = "company_code", referencedColumnName = "company_code", updatable = false, insertable = false) })

	private BranchMaster branchMaster;
}
