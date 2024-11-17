package com.profit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.profit.datamodel.RoleMapping;
import com.profit.dto.ResponseObject;
import com.profit.dto.RoleMappingDTO;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.repository.RoleMappingRepository;

import jakarta.transaction.Transactional;

@Service
public class RoleMappingService {

	@Autowired
	RoleMappingRepository roleMappingRepository;

	public ResponseObject<List<RoleMappingDTO>> getRoles(String company, String branch) {
		List<RoleMapping> entityList = roleMappingRepository.findAllByCompanyCodeAndBranchCode(company, branch);
		List<RoleMappingDTO> dtoList = new ArrayList<RoleMappingDTO>();

		for (RoleMapping entity : entityList) {
			RoleMappingDTO dto = new RoleMappingDTO();
			BeanUtils.copyProperties(entity, dto);
			dtoList.add(dto);
		}
		return ResponseObject.success(dtoList);
	}
	
	public ResponseObject<List<RoleMappingDTO>> getRolesByUserCode(String userCode, String company, String branch){
		List<RoleMapping> entityList = roleMappingRepository.findByUserCodeAndCompanyCodeAndBranchCode(userCode, company, branch);
		List<RoleMappingDTO> dtoList = new ArrayList<RoleMappingDTO>();
		
		for (RoleMapping entity : entityList) {
			RoleMappingDTO dto = new RoleMappingDTO();
			BeanUtils.copyProperties(entity, dto);
			dtoList.add(dto);
		}
		return ResponseObject.success(dtoList);
	}

	@Transactional
	public ResponseObject<List<RoleMappingDTO>> updateRoles(List<RoleMappingDTO> dtoList, String company, String branch,
			String username) {

		try {

			List<RoleMappingDTO> userRoles = new ArrayList<RoleMappingDTO>();
			for (RoleMappingDTO dto : dtoList) {
				if (dto.getId() != null) {
					if (dto.getIsDelete() != null && dto.getIsDelete() == true) {
						roleMappingRepository.deleteById(dto.getId());
					} else {
						dto.setLastModifiedBy(username);
						userRoles.add(dto);
					}
				} else {
					dto.setBranchCode(branch);
					dto.setCompanyCode(company);
					dto.setCreatedBy(username);
					userRoles.add(dto);
				}
			}

			List<RoleMapping> entityList = userRoles.stream().map(dto -> {
				RoleMapping entity = new RoleMapping();
				BeanUtils.copyProperties(dto, entity);
				return entity;
			}).collect(Collectors.toList());
			roleMappingRepository.saveAll(entityList);

			List<RoleMappingDTO> resultList = entityList.stream().map(entity -> {
				RoleMappingDTO dto = new RoleMappingDTO();
				BeanUtils.copyProperties(entity, dto);
				return dto;
			}).collect(Collectors.toList());
			

			return ResponseObject.success(resultList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}
}
