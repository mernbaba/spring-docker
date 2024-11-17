package com.profit.dto;

import java.io.Serializable;
import java.util.Date;

import org.antlr.v4.runtime.misc.NotNull;

import lombok.Data;

@Data
public class EmployeeDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;

	private String employeeId;

	private String referenceId;

//	@NotNull(message="Customer Name Cannot Be Empty")
	private String employeeName;

//	@NotNull(message="Phone Number Cannot Be Empty")
	private String phno;

//	@NotNull(message="Service Cannot Be Empty")
	private String service;
	
	private Date createddate;
}
