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
@Table(name = "tb_shift_master", uniqueConstraints = @UniqueConstraint(columnNames = { "shift_code", "branch_code", "company_code" }))
@Data
public class ShiftMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shift_id")
	private Long id;

	@Column(name = "shift_code", nullable = false, length = 56)
	private String shiftCode;

	@Column(name = "shift_name", nullable = false)
	private String shiftName;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
	
	@Column(name = "branch_code", nullable = false, length = 56)
	private String branchCode;
	
	@Column(name = "company_code", nullable = false, length = 56)
	private String companyCode;
	
	@Column(name = "createdby", nullable = false, length = 32)
	private String createdBy;
	
	@Column(name = "created_date")
	@CreationTimestamp
	private Timestamp createdDate;
	
	@Column(name = "last_modified_by", length = 32)
	private String lastModifiedBy;
	
	@Column(name = "last_modified_date")
	@UpdateTimestamp
	private Timestamp lastModifiedDate;
	

	@ManyToOne
	@JoinColumns({@JoinColumn(name = "branch_code", referencedColumnName = "branch_code", insertable = false, updatable = false),
			@JoinColumn(name = "company_code", referencedColumnName = "company_code", insertable = false, updatable = false)})
	private BranchMaster branchMaster;

}
