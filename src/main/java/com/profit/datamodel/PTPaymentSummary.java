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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_pt_payment_summary")
public class PTPaymentSummary implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "customer_code", nullable = false, length = 56)
	private String customerCode;

	@Column(name = "customer_name", nullable = false)
	private String customerName;
	
	@Column(name = "staff_code", length = 56, nullable = false)
	private String staffCode;

	@Column(name = "staff_name", nullable = false)
	private String staffName;

	@Column(name = "actual_amount")
	private BigDecimal actualAmount;

	@Column(name = "amount_paid")
	private BigDecimal amountPaid;

	@Column(name = "discont_amount")
	private BigDecimal discountAmount;

	@Column(name = "balance_amount")
	private BigDecimal balanceAmount;

	@Column(name = "settled")
	private Boolean settled;

	@Column(name = "pt_start_date_of_plan")
	private LocalDate ptStartDateOfPlan;
	
	@Column(name = "pt_end_date_of_plan")
	private LocalDate ptEndDateOfPlan;
	
	@Column(name = "pt_payment_plan", length = 100)
	private String ptPaymentPlan;
	
	@Column(name = "branch_code", nullable = false, length = 56)
	private String branchCode;

	@Column(name = "company_code", nullable = false, length = 56)
	private String companyCode;
	
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

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
	
	@PrePersist
	protected void onCreate() {
		if (isActive == null) {
			isActive = true;
		}
	}
	
	@ManyToOne
	@JoinColumn(name = "customer_code", referencedColumnName = "customer_code", insertable = false, updatable = false)
	private CustomerMaster customerMaster;
	
	@ManyToOne
	@JoinColumns({@JoinColumn(name = "branch_code", referencedColumnName = "branch_code", insertable = false, updatable = false),
			@JoinColumn(name = "company_code", referencedColumnName = "company_code", insertable = false, updatable = false)})
	private BranchMaster branchMaster;

}
