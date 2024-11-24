package com.profit.datamodel;

import java.io.Serializable;
import java.math.BigDecimal;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_pt_pricing_plan")
public class PTPricingPlans implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "staff_code", nullable = false, length = 56)
	private String staffCode;
	
	@Column(name = "staff_name", nullable = false)
	private String staffName;
	
	@Column(name = "plan_code", nullable = false)
	private Integer planCode;
	
	@Column(name = "plan_name", nullable = false)
	private String planName;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "amount", nullable = false)
	private BigDecimal amount;
	
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
		
	@Column(name = "company_code", nullable = false, length = 56)
	private String companyCode;
	
	@Column(name = "branch_code", nullable = false, length = 56)
	private String branchCode;
	
	@Column(name = "createdby", nullable = false, length = 32)
	private String createdBy;
	
	@Column(name = "created_date", nullable = false)
	@CreationTimestamp
	private LocalDate createdDate;
	
	@Column(name = "last_modified_by", length = 32)
	private String lastModifiedBy;
	
	@Column(name = "last_modified_date")
	@UpdateTimestamp
	private LocalDate lastModifiedDate;
	
	@ManyToOne
	@JoinColumns({@JoinColumn(name = "branch_code", referencedColumnName = "branch_code", insertable = false, updatable = false),
			@JoinColumn(name = "company_code", referencedColumnName = "company_code", insertable = false, updatable = false)})
	private BranchMaster branchMaster;
	
	@OneToOne
	@JoinColumn(name = "staff_code", referencedColumnName = "staff_code", insertable = false, updatable = false)
	private StaffMaster staffMaster;
	
	@PrePersist
	protected void onCreate() {
		if (isActive == null) {
			isActive = true;
		}
	}
	

}
