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
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_customer_payment_summary", uniqueConstraints = @UniqueConstraint(name = "uk_customer_repo", columnNames = {
		"customer_code", "plan_start_date", "plan_end_date", "branch_code", "company_code" }))
public class CustomerPaymentSummary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "customer_code", nullable = false)
	private String customerCode;

	@Column(name = "customer_name", nullable = false)
	private String customerName;

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

	@Column(name = "plan_start_date", nullable = false)
	private LocalDate planStartDate;

	@Column(name = "plan_end_date", nullable = false)
	private LocalDate planEndDate;
	
//	@Column(name = "has_pt", nullable = false)
//	private Boolean hasPt;
//	
//	@Column(name = "pt_name")
//	private Boolean ptName;
//	
//	@Column(name = "pt_start_date")
//	private LocalDate ptStartDate;
//	
//	@Column(name = "pt_end_date")
//	private LocalDate ptEndDate;
	
	@Column(name = "payment_plan", length = 56)
	private String paymentPlan;

	@Column(name = "branch_code", nullable = false, length = 56)
	private String branchCode;

	@Column(name = "company_code", nullable = false, length = 56)
	private String companyCode;

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
	@JoinColumn(name = "customer_code", referencedColumnName = "customer_code", insertable = false, updatable = false)
	private CustomerMaster customerMaster;
	
	@ManyToOne
	@JoinColumns({@JoinColumn(name = "branch_code", referencedColumnName = "branch_code", insertable = false, updatable = false),
			@JoinColumn(name = "company_code", referencedColumnName = "company_code", insertable = false, updatable = false)})
	private BranchMaster branchMaster;	
	
//	@PrePersist
//	protected void onCreate() {
//		if (hasPt == null) {
//			hasPt = false;
//		}
//	}

}
