package com.profit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.profilt.mapper.ShiftMasterMapper;
import com.profit.datamodel.ShiftMaster;
import com.profit.dto.ResponseObject;
import com.profit.dto.ShiftMasterDTO;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.repository.ShiftMasterRepository;

import jakarta.transaction.Transactional;

@Service
public class ShiftMasterService {
	
	@Autowired
	ShiftMasterRepository shiftMasterRepository;
	
//	@Autowired
//	ShiftMasterMapper shiftMasterMapper;

	public ResponseObject<List<ShiftMasterDTO>> getShits(String company, String branch) {
		List<ShiftMaster> entityList = shiftMasterRepository.findAllByCompanyCodeAndBranchCode(company, branch);
		List<ShiftMasterDTO> dtoList = new ArrayList<ShiftMasterDTO>();
		
		for(ShiftMaster entity : entityList) {
			ShiftMasterDTO dto = new ShiftMasterDTO();
			BeanUtils.copyProperties(entity, dto);
			dtoList.add(dto);
		}
		
		return ResponseObject.success(dtoList);
	}

	@Transactional
	public ResponseObject<ShiftMasterDTO> save(ShiftMasterDTO dto, String company, String branch, String username) {
		
		try {
			ShiftMaster entity = new ShiftMaster();
			BeanUtils.copyProperties(dto, entity);
			
			entity.setShiftCode(dto.getShiftCode());
			entity.setShiftName(dto.getShiftName());
			entity.setIsActive(dto.getIsActive());
			entity.setBranchCode(branch);
			entity.setCompanyCode(company);
			entity.setCreatedBy(username);
			
			shiftMasterRepository.save(entity);
			ShiftMasterDTO shiftMasterDTO = new ShiftMasterDTO();
			BeanUtils.copyProperties(entity, shiftMasterDTO);
//			ShiftMasterDTO shiftMasterDTO = shiftMasterMapper.toDto(entity);
			
			return ResponseObject.success(shiftMasterDTO);
		}catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
		
	}

	@Transactional
	public ResponseObject<ShiftMasterDTO> updateShift(ShiftMasterDTO dto, String company, String branch, String username) {
		
		try {
			if (dto == null || dto.getId() == null) {
				throw new CloudBaseException(ResponseCode.BAD_REQUEST);
			}
//			ShiftMaster entity = shiftMasterMapper.toEntity(dto);
			ShiftMaster entity = shiftMasterRepository.findById(dto.getId()).orElseThrow(() -> new CloudBaseException(ResponseCode.USER_NOT_FOUND));;
			BeanUtils.copyProperties(dto, entity);
			
			entity.setLastModifiedBy(username);
			
			shiftMasterRepository.save(entity);
//			ShiftMasterDTO shiftMasterDTO = shiftMasterMapper.toDto(entity);
			ShiftMasterDTO shiftMasterDTO = new ShiftMasterDTO();
			BeanUtils.copyProperties(entity, shiftMasterDTO);
			
			return ResponseObject.success(shiftMasterDTO);
		}catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
		
	}

}
