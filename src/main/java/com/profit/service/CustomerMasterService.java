package com.profit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.profit.datamodel.BranchMaster;
import com.profit.datamodel.CustomerMaster;
import com.profit.datamodel.PrefixGenerator;
import com.profit.datamodel.RoleMapping;
import com.profit.datamodel.SecUser;
import com.profit.dto.CustomerMasterDTO;
import com.profit.dto.ResponseObject;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.repository.BranchMasterRepository;
import com.profit.repository.CustomerMasterRepository;
import com.profit.repository.PrefixGeneratorRepository;
import com.profit.repository.RoleMappingRepository;
import com.profit.repository.SecUserRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerMasterService {

	private static long counter = 0;

	@Autowired
	CustomerMasterRepository customerMasterRepository;

	@Autowired
	BranchMasterRepository branchMasterRepository;

	@Autowired
	PrefixGeneratorRepository prefixGeneratorRepository;

	@Autowired
	SecUserRepository secUserRepository;

	@Autowired
	RoleMappingRepository roleMappingRepository;

	public ResponseObject<List<CustomerMasterDTO>> getAll(String company, String branch) {
		List<CustomerMaster> customerMasterEntitys = customerMasterRepository.findAllByCompanyCodeAndBranchCode(company,
				branch);
		List<CustomerMasterDTO> dtoList = new ArrayList<CustomerMasterDTO>();

		for (CustomerMaster entity : customerMasterEntitys) {
			CustomerMasterDTO dto = new CustomerMasterDTO();
			BeanUtils.copyProperties(entity, dto);
			dtoList.add(dto);
		}

		return ResponseObject.success(dtoList);
	}

	@Transactional
	public ResponseObject<CustomerMasterDTO> saveCustomer(CustomerMasterDTO dto, String company, String branch,
			String username) {
		ResponseCode error = null;
		try {
			CustomerMaster master = customerMasterRepository.findByPhoneNumber(dto.getPhoneNumber());
			if (master != null) {
				error = ResponseCode.USER_NAME_ALREADY_EXISTS;
				throw new CloudBaseException(error);
			} else {
				CustomerMaster entity = new CustomerMaster();
				PrefixGenerator prefix = prefixGeneratorRepository.findByBranchCodeAndCompanyCodeAndPrefixCode(branch,
						company, "CUSTOMER");
				if (prefix != null) {
					entity.setCustomerCode(
							prefix.getPrefix() + "-" + String.format("%03d", prefix.getLastGenerated() + 1));
					prefix.setLastGenerated(prefix.getLastGenerated() + 1);
					prefixGeneratorRepository.save(prefix);
				} else {
					PrefixGenerator prefixEntity = new PrefixGenerator();
					prefixEntity.setBranchCode(branch);
					prefixEntity.setCompanyCode(company);
					prefixEntity.setPadLength("3");
					prefixEntity.setPrefixCode("CUSTOMER");
					BranchMaster branchmaster = branchMasterRepository.findByBranchCodeAndCompanyCode(branch, company);
					prefixEntity.setPrefix((branchmaster.getShortName() + "C").toUpperCase());
					prefixEntity.setPrefixDate(new Date());
					prefixEntity.setLastGenerated(1L);
					prefixGeneratorRepository.save(prefixEntity);
					entity.setCustomerCode(
							prefixEntity.getPrefix() + "-" + String.format("%03d", prefixEntity.getLastGenerated()));
				}
				entity.setAddress(dto.getAddress());
				entity.setCustomerName(dto.getCustomerName());
				entity.setStartDateOfPlan(dto.getStartDateOfPlan());
				entity.setEndDateOfPlan(dto.getEndDateOfPlan());
				entity.setGender(dto.getGender());
				entity.setHasPT(dto.getHasPT());
				if (dto.getHasPT()) {
					entity.setStaffName(dto.getStaffName());
				} else {
					entity.setStaffName(null);
				}
				entity.setPhoneNumber(dto.getPhoneNumber());
				entity.setEmail(dto.getEmail());
				entity.setProfilePic(dto.getProfilePic());
				entity.setPaymentPlan(dto.getPaymentPlan());
				entity.setWorkoutPlan(dto.getWorkoutPlan());
				entity.setShift(dto.getShift());
				entity.setWeight(dto.getWeight());
				entity.setIsActive(dto.getIsActive());
				entity.setBranchCode(branch);
				entity.setCompanyCode(company);
				entity.setCreatedBy(username);

				customerMasterRepository.save(entity);

				RoleMapping role = new RoleMapping();
				role.setRoleCode("CUSTOMER");
				role.setUserCode(entity.getCustomerCode());
				role.setBranchCode(branch);
				role.setCompanyCode(company);
				role.setCreatedBy(username);

				roleMappingRepository.save(role);

				CustomerMasterDTO cust = new CustomerMasterDTO();
				BeanUtils.copyProperties(entity, cust);
				return ResponseObject.success(cust);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

//	public static String generateShortCode(String name) {
//
//		String[] nameParts = name.trim().split(" ");
//		StringBuilder initials = new StringBuilder();
//
//		initials.append(nameParts[0].toUpperCase());
//		counter++; // Increment the counter for each new code
//		String shortCode = String.format("%s-%03d", initials, counter);
//
//		return shortCode;
//	}

	@Transactional
	public ResponseObject<CustomerMasterDTO> updateCustomer(CustomerMasterDTO dto, String company, String branch,
			String username) {

		try {

			Optional<CustomerMaster> customer = customerMasterRepository.findById(dto.getId());
			CustomerMaster entity = new CustomerMaster();

			if (customer.isPresent()) {
				entity = customer.get();
				BeanUtils.copyProperties(dto, entity);
				if (!dto.getHasPT()) {
					entity.setStaffName(null);
				}
				entity.setCompanyCode(company);
				entity.setBranchCode(branch);
				entity.setLastModifiedBy(username);
				entity = customerMasterRepository.save(entity);
			}

			SecUser secUser = secUserRepository.findByUserCode(entity.getCustomerCode());
			if (secUser != null && ObjectUtils.isNotEmpty(secUser)) {

				if (!secUser.getEmail().equalsIgnoreCase(entity.getEmail())) {
					secUser.setEmail(entity.getEmail());
				}
				if (!secUser.getPhoneNumber().equals(entity.getPhoneNumber())) {
					secUser.setPhoneNumber(entity.getPhoneNumber());
//					secUser.setUserName(entity.getPhoneNumber());
				}
				secUserRepository.save(secUser);

			}

			CustomerMasterDTO custDto = new CustomerMasterDTO();
			BeanUtils.copyProperties(entity, custDto);
//			custDto.setLastModifiedBy(entity.getLastModifiedBy());
//			custDto.setLastModifiedDate(entity.getLastModifiedDate());

			return ResponseObject.success(custDto);

		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

}
