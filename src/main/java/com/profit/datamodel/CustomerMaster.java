package com.profit.datamodel;

import java.io.Serializable;
import java.sql.Timestamp;
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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "customer_master")
public class CustomerMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "customer_code", nullable = false, length = 56, unique = true)
	private String customerCode;

	@Column(name = "customer_name", nullable = false, length = 255)
	private String customerName;

	@Column(name = "phone_number", unique = true, nullable = false, length = 20)
	private String phoneNumber;
	
	@Column(name = "email", unique = true)
	private String email;

	@Lob // Large Object for profile picture (binary data)
	@Column(name = "profile_pic")
	private byte[] profilePic;

	@Column(name = "payment_plan", length = 100)
	private String paymentPlan;

	@Column(name = "start_date_of_plan")
	private LocalDate startDateOfPlan;

	@Column(name = "end_date_of_plan")
	private LocalDate endDateOfPlan;

	@Column(name = "address", length = 500)
	private String address;

	@Column(name = "workout_plan", length = 100)
	private String workoutPlan;

	@Column(name = "gender", length = 10) // Adjust length based on expected values (e.g., "Male", "Female")
	private String gender;

	@Column(name = "has_pt")
	private Boolean hasPT;
	
	@Column(name = "staff_code", length = 56)
	private String staffCode;

	@Column(name = "staff_name", length = 255)
	private String staffName;

	@Column(name = "shift", length = 50)
	private String shift;

	@Column(name = "weight")
	private Float weight;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
	
	@Column(name = "company_code")
	private String companyCode;
	
	@Column(name = "branch_code")
	private String branchCode;
	
	@Column(name = "createdby", length = 32)
	private String createdBy;
	
	@Column(name = "created_date")
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
}
