package com.profit.datamodel;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee implements Serializable {

	/**
		* 
		*/
			private static final long serialVersionUID = 1L;
			@Id
			@GeneratedValue(strategy = GenerationType.IDENTITY)
			
			@Column(name = "id", nullable = false)
			private Long id;
			
			@Column(name = "employee_id")
			private String employeeId;
			
			@Column(name = "reference_id", nullable = false)
			private String referenceId;
			
			@Column(name = "employee_name", nullable = false)
			private String employeeName;
			
			@Column(name = "phno", nullable = false)
			private String phno;
			
			@Column(name = "service", nullable = false)
			private String service;
			
			@Column(name="createddate",nullable=false)
			private Date createddate;
			
			}
			