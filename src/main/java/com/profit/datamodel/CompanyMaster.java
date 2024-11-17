package com.profit.datamodel;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "company_master"/*,uniqueConstraints = @UniqueConstraint(columnNames = "", name = )*/)
@Data
public class CompanyMaster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long companyId;

	@Column(name = "company_code", unique = true, nullable = false, length = 56)
	private String companyCode;

	@Column(name = "company_name", nullable = false, length = 255)
	private String companyName;

	@Column(name = "pricing_code", length = 56)
	private String pricingCode;

	@Column(name = "valid_till")
	private Date validTill;

	@Column(name = "short_name", length = 3)
	private String shortName;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;

	@Column(name = "is_multi_branch", nullable = false)
	private Boolean isMultiBranch = false;

	@Column(name = "logo_name", length = 100)
	private String logoName;

	@Column(name = "file_dir", length = 255)
	private String fileDir;

	@Column(name = "stamp_file_dir", length = 255)
	private String stampFileDir;

	@Column(name = "pan", length = 56)
	private String pan;

	@Column(name = "tan", length = 56)
	private String tan;

	@Column(name = "created_by", nullable = false, length = 12)
	private String createdBy;

	@Column(name = "created_date", nullable = false)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "last_modified_by", length = 12)
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	@UpdateTimestamp
	private Date lastModifiedDate;

}

