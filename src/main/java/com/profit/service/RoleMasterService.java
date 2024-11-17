package com.profit.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.profit.datamodel.RoleMaster;
import com.profit.dto.ResponseObject;
import com.profit.dto.RoleMasterDTO;
import com.profit.repository.RoleMasterRepository;

@Service
public class RoleMasterService {
	
	@Autowired
	RoleMasterRepository roleMasterRepository;

	public ResponseObject<List<RoleMasterDTO>> getAllRoles() {
		
		List<RoleMasterDTO> dtoList = roleMasterRepository.findAll().stream().map(entity -> {
			RoleMasterDTO dto = new RoleMasterDTO();
			BeanUtils.copyProperties(entity, dto);
			return dto;
		}).collect(Collectors.toList());
		
		return ResponseObject.success(dtoList);
		
	}

}
