package com.profit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.profit.datamodel.*;
import com.profit.repository.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.profit.datamodel.CustomerReport;
import com.profit.dto.CustomerMasterDTO;
import com.profit.dto.CustomerPaymentSummaryDTO;
import com.profit.dto.ResponseObject;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;

import jakarta.transaction.Transactional;

@Service
public class CustomerMasterService {

//	private static long counter = 0;

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

	@Autowired
	CustomerPaymentDetailsRepository customerPaymentDetailsRepository;

	@Autowired
	CustomerPaymentSummaryRepository customerPaymentSummeryRepository;
	
	@Autowired
	PTPaymentSummaryRepository ptPaymentSummaryRepository;

	@Autowired
	PTPaymentDetailsRepository ptPaymentDetailsRepository;
	
	@Autowired
	StaffMasterRepository staffMasterRepository;

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
			CustomerMaster customerEntity = customerMasterRepository.findByPhoneNumber(dto.getPhoneNumber());
			if (customerEntity != null) {
				error = ResponseCode.MOBILE_NUMBER_ALREADY_EXISTS;
				throw new CloudBaseException(error);
			}
			StaffMaster staffMaster = staffMasterRepository.findByPhone(dto.getPhoneNumber());
			if (staffMaster != null) {
				error = ResponseCode.MOBILE_NUMBER_ALREADY_EXISTS;
				throw new CloudBaseException(error);
			}

			customerEntity = new CustomerMaster();

			PrefixGenerator prefix = prefixGeneratorRepository.findByBranchCodeAndCompanyCodeAndPrefixCode(branch,
					company, "CUSTOMER");
			if (prefix != null) {
				customerEntity.setCustomerCode(
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
				customerEntity.setCustomerCode(
						prefixEntity.getPrefix() + "-" + String.format("%03d", prefixEntity.getLastGenerated()));
			}
			customerEntity.setAddress(dto.getAddress());
			customerEntity.setCustomerName(dto.getCustomerName());
			customerEntity.setStartDateOfPlan(dto.getStartDateOfPlan());
			customerEntity.setEndDateOfPlan(dto.getEndDateOfPlan());
			customerEntity.setGender(dto.getGender());
			customerEntity.setHasPT(dto.getHasPT());
			if (dto.getHasPT()) {
				customerEntity.setStaffCode(dto.getStaffCode());
				customerEntity.setStaffName(dto.getStaffName());
				customerEntity.setPtStartDateOfPlan(dto.getPtStartDateOfPlan());
				customerEntity.setPtEndDateOfPlan(dto.getPtEndDateOfPlan());
				customerEntity.setPtPaymentPlan(dto.getPtPaymentPlan());
			} else {
				customerEntity.setStaffName(null);
			}
			customerEntity.setPhoneNumber(dto.getPhoneNumber());
			customerEntity.setEmail(dto.getEmail());
			customerEntity.setProfilePic(dto.getProfilePic());
			customerEntity.setPaymentPlan(dto.getPaymentPlan());
			customerEntity.setWorkoutPlan(dto.getWorkoutPlan());
			customerEntity.setShift(dto.getShift());
			customerEntity.setWeight(dto.getWeight());
			customerEntity.setIsActive(dto.getIsActive());
			customerEntity.setBranchCode(branch);
			customerEntity.setCompanyCode(company);
			customerEntity.setCreatedBy(username);

			customerMasterRepository.save(customerEntity);
			
			if(dto.getHasPT()) {
				PTPaymentSummary ptSummaryEntity = new PTPaymentSummary();
				
				BeanUtils.copyProperties(dto.getPtPaymentSummaryDTO(), ptSummaryEntity);
				
				ptSummaryEntity.setCustomerCode(customerEntity.getCustomerCode());
				ptSummaryEntity.setCustomerName(customerEntity.getCustomerName());
				ptSummaryEntity.setStaffCode(customerEntity.getStaffCode());
				ptSummaryEntity.setStaffName(customerEntity.getStaffName());
				ptSummaryEntity.setPtStartDateOfPlan(customerEntity.getPtStartDateOfPlan());
				ptSummaryEntity.setPtEndDateOfPlan(customerEntity.getPtEndDateOfPlan());
				ptSummaryEntity.setPtPaymentPlan(customerEntity.getPtPaymentPlan());
				ptSummaryEntity.setCompanyCode(company);
				ptSummaryEntity.setBranchCode(branch);
				ptSummaryEntity.setCreatedBy(username);

				Long id = ptPaymentSummaryRepository.save(ptSummaryEntity).getId();

				List<PTPaymentDetails> detailsEntList = new ArrayList<>();

				if(dto.getPtPaymentSummaryDTO().getPtPaymentDetails() != null) {

					dto.getPtPaymentSummaryDTO().getPtPaymentDetails().forEach(details -> {
						PTPaymentDetails detailsEntity = new PTPaymentDetails();
						BeanUtils.copyProperties(details, detailsEntity);
						detailsEntity.setPtPaymentSummeryId(id);
						detailsEntity.setCreatedBy(username);
						detailsEntList.add(detailsEntity);
					});

					ptPaymentDetailsRepository.saveAll(detailsEntList);
				}
			}

			CustomerPaymentSummary reportEntity = new CustomerPaymentSummary();
			
			BeanUtils.copyProperties(dto.getCustomerPaymentSummaryDTO(), reportEntity);

			reportEntity.setCustomerCode(customerEntity.getCustomerCode());
			reportEntity.setCustomerName(customerEntity.getCustomerName());
			reportEntity.setPlanStartDate(customerEntity.getStartDateOfPlan());
			reportEntity.setPlanEndDate(customerEntity.getEndDateOfPlan());
			reportEntity.setPaymentPlan(customerEntity.getPaymentPlan());
			reportEntity.setCompanyCode(company);
			reportEntity.setBranchCode(branch);
			reportEntity.setCreatedBy(username);

			Long id = customerPaymentSummeryRepository.save(reportEntity).getId();
			List<CustomerPaymentDetails> detailsEntList = new ArrayList<CustomerPaymentDetails>();
			
			if(dto.getCustomerPaymentSummaryDTO().getCustomerPaymentDetails() != null) {
				
				dto.getCustomerPaymentSummaryDTO().getCustomerPaymentDetails().forEach(details -> {
					CustomerPaymentDetails detailsEntity = new CustomerPaymentDetails();
					BeanUtils.copyProperties(details, detailsEntity);
					detailsEntity.setPaymentSummeryId(id);
					detailsEntity.setCreatedBy(username);
					detailsEntList.add(detailsEntity);
				});
				
				customerPaymentDetailsRepository.saveAll(detailsEntList);
			}

			RoleMapping role = new RoleMapping();
			role.setRoleCode("CUSTOMER");
			role.setUserCode(customerEntity.getCustomerCode());
			role.setBranchCode(branch);
			role.setCompanyCode(company);
			role.setCreatedBy(username);

			roleMappingRepository.save(role);

			CustomerMasterDTO cust = new CustomerMasterDTO();
			BeanUtils.copyProperties(customerEntity, cust);

			return ResponseObject.success(cust);
		
		} catch (CloudBaseException exp) {
			exp.printStackTrace();
			throw exp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}


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
