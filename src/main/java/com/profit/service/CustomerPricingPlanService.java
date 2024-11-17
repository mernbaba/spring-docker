package com.profit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.profit.datamodel.CustomerPricingPlan;
import com.profit.dto.CustomerPricingPlanDTO;
import com.profit.dto.ResponseObject;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.repository.CustomerPricingPlanRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerPricingPlanService {
	
	@Autowired
	CustomerPricingPlanRepository customerPricingPlanRepository;

	public ResponseObject<List<CustomerPricingPlanDTO>> getAllPricingPlans(String company, String branch) {
		List<CustomerPricingPlan> customerPricingPlans = customerPricingPlanRepository.findAllByCompanyCodeAndBranchCode(company, branch);
		List<CustomerPricingPlanDTO> dtoList = new ArrayList();
		
		for(CustomerPricingPlan entity : customerPricingPlans) {
			CustomerPricingPlanDTO dto = new CustomerPricingPlanDTO();
			BeanUtils.copyProperties(entity, dto);
			dtoList.add(dto);
		}
		
		return ResponseObject.success(dtoList);
	}

	@Transactional
	public ResponseObject<CustomerPricingPlanDTO> save(CustomerPricingPlanDTO dto, String company, String branch, String username) {
		
		try {
			CustomerPricingPlan entity = new CustomerPricingPlan();
			BeanUtils.copyProperties(dto, entity);
			entity.setPlanCode(dto.getPlanCode());
			entity.setPlanName(dto.getPlanName());
			entity.setDescription(dto.getDescription());
			entity.setAmount(dto.getAmount());
			entity.setIsActive(dto.getIsActive());
			entity.setBranchCode(branch);
			entity.setCompanyCode(company);
			entity.setCreatedBy(username);
			
			customerPricingPlanRepository.save(entity);
			
			CustomerPricingPlanDTO customerPricingPlanDTO = new CustomerPricingPlanDTO();
			BeanUtils.copyProperties(entity, customerPricingPlanDTO);
			
			return ResponseObject.success(customerPricingPlanDTO);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

	@Transactional
	public ResponseObject<CustomerPricingPlanDTO> updatePricingPlans(CustomerPricingPlanDTO dto, String company, String branch, String username) {
		try {
			if (dto == null || dto.getId() == null) {
				throw new CloudBaseException(ResponseCode.BAD_REQUEST);
			}
			 CustomerPricingPlan plan = customerPricingPlanRepository.findById(dto.getId()).orElseThrow(() -> new CloudBaseException(ResponseCode.USER_NOT_FOUND));
			 
			 BeanUtils.copyProperties(dto, plan);
			 plan.setLastModifiedBy(username);
			 
			 customerPricingPlanRepository.save(plan);
			 
			 CustomerPricingPlanDTO planDto = new CustomerPricingPlanDTO();
			 BeanUtils.copyProperties(plan, planDto);
			return ResponseObject.success(planDto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
		
	}
}
