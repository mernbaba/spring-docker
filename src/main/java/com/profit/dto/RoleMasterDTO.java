package com.profit.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class RoleMasterDTO {
	
	private Long roleId;
	private String roleCode;
	private Boolean isActive;

}
