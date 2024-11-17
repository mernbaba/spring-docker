package com.profit.datamodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
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
@Data
@Table(name = "tb_customer_pricing_plan", uniqueConstraints = @UniqueConstraint(name= "uk_price_palns", columnNames = { "plan_name", "branch_code", "company_code" }))
public class CustomerPricingPlan implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pricing_plan_id")
	private Long id;

	@Column(name = "plan_code", nullable = false)
	private Integer planCode;

	@Column(name = "plan_name", nullable = false)
	private String planName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "amount", nullable = false)
	private Double amount;

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