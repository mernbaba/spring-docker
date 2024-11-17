package com.profit.datamodel;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "prefix_generator")
@Data
public class PrefixGenerator implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pref_code_id")
	private Long id;
	
	@Column(name = "company_code",nullable=false)
	private String companyCode;
	
//	@OneToOne
//	@JoinColumn(name="branch_code",referencedColumnName = "branch_code")
	@Column(name = "branch_code",nullable=false)
	private String branchCode;
	
	@Column(name = "prefix_code",nullable=false)
	private String prefixCode;
	
	@Column(name = "prefix_date",nullable=false)
	private Date prefixDate;
	
	@Column(name = "prefix",nullable=false)
	private String prefix;
	
	@Column(name = "last_generated")
	private Long lastGenerated;
	
	@Column(name="pad_length")
	private String padLength;
}
