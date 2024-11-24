package com.profit.datamodel;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_customer_payment_details")
public class CustomerPaymentDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "payment_summery_id", nullable = false)
	private Long paymentSummeryId;

	@Column(name = "payment_mode", nullable = false)
	private String paymentMode;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "created_by", nullable = false, length = 56)
	private String createdBy;

	@Column(name = "created_date", nullable = false)
	@CreationTimestamp
	private LocalDate createdDate;

	@Column(name = "last_modified_by", length = 56)
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	@UpdateTimestamp
	private LocalDate lastModifiedDate;

	@ManyToOne
	@JoinColumn(name = "payment_summery_id", referencedColumnName = "id", insertable = false, updatable = false)
	CustomerPaymentSummary customerPaymentSummary;

}
