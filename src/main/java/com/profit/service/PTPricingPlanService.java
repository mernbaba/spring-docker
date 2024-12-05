package com.profit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.profit.datamodel.PTPricingPlans;
import com.profit.dto.PTPricingPlanDTO;
import com.profit.dto.ResponseObject;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.repository.PTPricingPlanRepository;

import jakarta.transaction.Transactional;

@Service
public class PTPricingPlanService {
	
	@Autowired
	PTPricingPlanRepository ptPricingPlanRepository;

	public ResponseObject<List<PTPricingPlanDTO>> getAllPlans(String company, String branch) {
		List<PTPricingPlans> ptPricingPlans = ptPricingPlanRepository.findAllByCompanyCodeAndBranchCode(company, branch);
		List<PTPricingPlanDTO> dtoList = new ArrayList<>();
		
		for(PTPricingPlans entity : ptPricingPlans) {
			PTPricingPlanDTO dto = new PTPricingPlanDTO();
			BeanUtils.copyProperties(entity, dto);
			dtoList.add(dto);
		}
		
		return ResponseObject.success(dtoList);
	}
	
	public ResponseObject<?> getStaffPlans(String staffCode, String branch, String company) {
		List<PTPricingPlans> ptPricingPlans = ptPricingPlanRepository.findByStaffCodeAndCompanyCodeAndBranchCode(staffCode, company, branch);
		List<PTPricingPlanDTO> dtoList = new ArrayList<>();
		
		for(PTPricingPlans entity : ptPricingPlans) {
			PTPricingPlanDTO dto = new PTPricingPlanDTO();
			BeanUtils.copyProperties(entity, dto);
			dtoList.add(dto);
		}
		
		return ResponseObject.success(dtoList);
		
	}

	@Transactional
	public ResponseObject<PTPricingPlanDTO> savePlan(PTPricingPlanDTO dto, String company, String branch,
			String username) {
		try {
			PTPricingPlans entity = new PTPricingPlans();
			BeanUtils.copyProperties(dto, entity);
			
			entity.setBranchCode(branch);
			entity.setCompanyCode(company);
			entity.setCreatedBy(username);
			
			ptPricingPlanRepository.save(entity);
			
			PTPricingPlanDTO ptPricingPlanDTO = new PTPricingPlanDTO();
			BeanUtils.copyProperties(entity, ptPricingPlanDTO);
			
			return ResponseObject.success(ptPricingPlanDTO);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

	@Transactional
	public ResponseObject<PTPricingPlanDTO> updatePricingPlans(PTPricingPlanDTO dto, String company, String branch,
			String username) {
		try {
			PTPricingPlans entity = new PTPricingPlans();
			BeanUtils.copyProperties(dto, entity);
			
			entity.setLastModifiedBy(username);
			
			ptPricingPlanRepository.save(entity);
			
			PTPricingPlanDTO ptPricingPlanDTO = new PTPricingPlanDTO();
			BeanUtils.copyProperties(entity, ptPricingPlanDTO);
			
			return ResponseObject.success(ptPricingPlanDTO);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

}
