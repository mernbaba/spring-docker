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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_pt_payment_details")
public class PTPaymentDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "pt_payment_summery_id", nullable = false)
	private Long ptPaymentSummeryId;

	@Column(name = "payment_mode", nullable = false)
	private String paymentMode;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "createdby", nullable = false, length = 56)
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
	@JoinColumn(name = "pt_payment_summery_id", referencedColumnName = "id", insertable = false, updatable = false)
	PTPaymentSummary ptPaymentSummary;

}
