package com.profit.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.profit.datamodel.CustomerMaster;
import com.profit.datamodel.CustomerPaymentDetails;
import com.profit.datamodel.CustomerPaymentSummary;
import com.profit.datamodel.PTPaymentDetails;
import com.profit.datamodel.PTPaymentSummary;
import com.profit.dto.CustomerPaymentDetailsDTO;
import com.profit.dto.CustomerPaymentSummaryDTO;
import com.profit.dto.PTPaymentDetailsDTO;
import com.profit.dto.PTPaymentSummaryDTO;
import com.profit.dto.ResponseObject;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.repository.CustomerMasterRepository;
import com.profit.repository.CustomerPaymentDetailsRepository;
import com.profit.repository.CustomerPaymentSummaryRepository;
import com.profit.repository.PTPaymentDetailsRepository;
import com.profit.repository.PTPaymentSummaryRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerPaymentSummaryService {

	@Autowired
	CustomerPaymentSummaryRepository customerPaymentSummaryRepository;

	@Autowired
	CustomerPaymentDetailsRepository customerPaymentDetailsRepository;

	@Autowired
	CustomerMasterRepository customerMasterRepository;
	
	@Autowired
	PTPaymentSummaryRepository ptPaymentSummaryRepository;
	

	public ResponseObject<List<CustomerPaymentSummaryDTO>> getUsers(String fromDate, String toDate, String customerCode,
			String branch, String company) {

		try {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			LocalDate fromDt = LocalDate.parse(fromDate, format);
			LocalDate toDt = LocalDate.parse(toDate, format);

			List<CustomerPaymentSummaryDTO> dtoList = new ArrayList<CustomerPaymentSummaryDTO>();

			List<CustomerPaymentSummary> paymentEntityList = new ArrayList<CustomerPaymentSummary>();

			if (customerCode.equalsIgnoreCase("ALL")) {

				paymentEntityList = customerPaymentSummaryRepository.getCustomersByPlanDates(fromDt, toDt, branch,
						company);
			} else {
				paymentEntityList = customerPaymentSummaryRepository.getCustomersByPlanDatesWithCustomer(fromDt, toDt,
						customerCode, branch, company);
			}

			List<Long> paymentIds = paymentEntityList.stream().map(CustomerPaymentSummary::getId)
					.collect(Collectors.toList());

			List<CustomerPaymentDetails> detailsList = customerPaymentDetailsRepository.getDataBySummaryIds(paymentIds);

			Map<Long, List<CustomerPaymentDetailsDTO>> detailsMap = detailsList.stream().map(detailsEntity -> {
				CustomerPaymentDetailsDTO detailsDto = new CustomerPaymentDetailsDTO();
				BeanUtils.copyProperties(detailsEntity, detailsDto);
				return detailsDto;
			}).collect(Collectors.groupingBy(CustomerPaymentDetailsDTO::getPaymentSummeryId));

			if (paymentEntityList != null) {
				paymentEntityList.forEach(entity -> {
					CustomerPaymentSummaryDTO dto = new CustomerPaymentSummaryDTO();
					BeanUtils.copyProperties(entity, dto);
					List<CustomerPaymentDetailsDTO> paymentDetailsList = detailsMap.getOrDefault(entity.getId(),
							new ArrayList<>());
//					detailsList.stream().filter(details -> details.getPaymentSummeryId().equals(entity.getId()))
//							.forEach(detailsEntity -> {
//								CustomerPaymentDetailsDTO detailsDto = new CustomerPaymentDetailsDTO();
//								BeanUtils.copyProperties(detailsEntity, detailsDto);
//								paymentDetailsList.add(detailsDto);
//							});
					dto.setCustomerPaymentDetails(paymentDetailsList);
					dtoList.add(dto);
				});
			}
			return ResponseObject.success(dtoList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.BAD_REQUEST);
		}

	}

	public ResponseObject<List<CustomerPaymentSummaryDTO>> getUserPayments(String customerCode, String branch,
			String company) {
		List<CustomerPaymentSummaryDTO> dtoList = new ArrayList<CustomerPaymentSummaryDTO>();

		customerPaymentSummaryRepository.findAllByBranchCodeAndCompanyCodeAndCustomerCode(branch, company, customerCode)
				.forEach(entity -> {
					CustomerPaymentSummaryDTO dto = new CustomerPaymentSummaryDTO();
					BeanUtils.copyProperties(entity, dto);
					dtoList.add(dto);
				});
		return ResponseObject.success(dtoList);
	}

	@Transactional
	public ResponseObject<CustomerPaymentSummaryDTO> save(CustomerPaymentSummaryDTO dto, String branch, String company,
			String username) {

		try {

			CustomerPaymentSummary entity = new CustomerPaymentSummary();
			BeanUtils.copyProperties(dto, entity);

			entity.setCompanyCode(company);
			entity.setBranchCode(branch);
			entity.setCreatedBy(username);

			Long id = customerPaymentSummaryRepository.save(entity).getId();
			//
			List<CustomerPaymentSummary> summaryList = customerPaymentSummaryRepository
					.getLatestSummaryOfCustomer(entity.getCustomerCode(), LocalDate.now());
			if (summaryList.size() > 0 && !summaryList.isEmpty()) {

				Optional<CustomerMaster> master = customerMasterRepository.findByCustomerCode(entity.getCustomerCode());
				if (master.isPresent()) {
					CustomerMaster cust = master.get();

					if (summaryList.size() > 1) {
						CustomerPaymentSummary summ = summaryList.stream()
								.max(Comparator.comparingLong(CustomerPaymentSummary::getId)).get();

						cust.setStartDateOfPlan(summ.getPlanStartDate());
						cust.setEndDateOfPlan(summ.getPlanEndDate());
						cust.setPaymentPlan(summ.getPaymentPlan());

					} else {
						cust.setStartDateOfPlan(summaryList.get(0).getPlanStartDate());
						cust.setEndDateOfPlan(summaryList.get(0).getPlanEndDate());
						cust.setPaymentPlan(summaryList.get(0).getPaymentPlan());
					}
					customerMasterRepository.save(cust);

				}

			}

			/*CustomerMaster customerMaster = customerMasterRepository.findByCustomerCode(dto.getCustomerCode()).get();
			
			if (dto.getPlanStartDate().isAfter(customerMaster.getEndDateOfPlan())) {
			
				customerMaster.setStartDateOfPlan(dto.getPlanStartDate());
				customerMaster.setEndDateOfPlan(dto.getPlanEndDate());
				customerMaster.setLastModifiedBy(username);
			
				customerMasterRepository.save(customerMaster);
			
			}*/

		

			List<CustomerPaymentDetails> detailtsEntityList = new ArrayList<CustomerPaymentDetails>();
			if (dto.getCustomerPaymentDetails() != null && dto.getCustomerPaymentDetails().size() > 0) {
				dto.getCustomerPaymentDetails().forEach(details -> {
					CustomerPaymentDetails detailsEnt = new CustomerPaymentDetails();
					BeanUtils.copyProperties(details, detailsEnt);
					detailsEnt.setPaymentSummeryId(id);
					detailsEnt.setCreatedBy(username);
					detailtsEntityList.add(detailsEnt);
				});

				customerPaymentDetailsRepository.saveAll(detailtsEntityList);
			}

			CustomerPaymentSummaryDTO responseDTO = new CustomerPaymentSummaryDTO();
			BeanUtils.copyProperties(entity, responseDTO);
			return ResponseObject.success(responseDTO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

	@Transactional
	public ResponseObject<CustomerPaymentSummaryDTO> update(CustomerPaymentSummaryDTO dto, String branch,
			String company, String username) {

		try {

			CustomerPaymentSummary entity = customerPaymentSummaryRepository.findById(dto.getId())
					.orElseThrow(() -> new CloudBaseException(ResponseCode.USER_NOT_FOUND));

			BeanUtils.copyProperties(dto, entity);

			entity.setLastModifiedBy(username);

			Long id = customerPaymentSummaryRepository.save(entity).getId();

			//
			List<CustomerPaymentSummary> summaryList = customerPaymentSummaryRepository
					.getLatestSummaryOfCustomer(entity.getCustomerCode(), LocalDate.now());
			if (summaryList.size() > 0 && !summaryList.isEmpty()) {

				Optional<CustomerMaster> master = customerMasterRepository.findByCustomerCode(entity.getCustomerCode());
				if (master.isPresent()) {
					CustomerMaster cust = master.get();

					if (summaryList.size() > 1) {
						CustomerPaymentSummary summ = summaryList.stream()
								.max(Comparator.comparingLong(CustomerPaymentSummary::getId)).get();

						cust.setStartDateOfPlan(summ.getPlanStartDate());
						cust.setEndDateOfPlan(summ.getPlanEndDate());
						cust.setPaymentPlan(summ.getPaymentPlan());

					} else {
						cust.setStartDateOfPlan(summaryList.get(0).getPlanStartDate());
						cust.setEndDateOfPlan(summaryList.get(0).getPlanEndDate());
						cust.setPaymentPlan(summaryList.get(0).getPaymentPlan());
					}
					customerMasterRepository.save(cust);

				}

			}

//			entity = customerPaymentSummaryRepository.getLatestRecordOfCustomer(entity.getCustomerCode());
//			if (entity != null && ObjectUtils.isNotEmpty(entity)) {
//				Optional<CustomerMaster> master = customerMasterRepository.findByCustomerCode(entity.getCustomerCode());
//				if (master.isPresent()) {
//					CustomerMaster cust = master.get();
//					cust.setStartDateOfPlan(entity.getPlanStartDate());
//					cust.setEndDateOfPlan(entity.getPlanEndDate());
//					cust.setPaymentPlan(entity.getPaymentPlan());
//				}
//			}

			List<CustomerPaymentDetails> detailtsEntityList = new ArrayList<CustomerPaymentDetails>();
			if (dto.getCustomerPaymentDetails() != null) {
				dto.getCustomerPaymentDetails().forEach(details -> {
					CustomerPaymentDetails detailsEnt = new CustomerPaymentDetails();
					BeanUtils.copyProperties(details, detailsEnt);
					if (detailsEnt.getId() == null) {
						detailsEnt.setPaymentSummeryId(id);
						detailsEnt.setCreatedBy(username);

					} else {
						detailsEnt.setLastModifiedBy(username);
					}
					detailtsEntityList.add(detailsEnt);
				});

				customerPaymentDetailsRepository.saveAll(detailtsEntityList);
			}

			CustomerPaymentSummaryDTO responseDTO = new CustomerPaymentSummaryDTO();
			BeanUtils.copyProperties(entity, responseDTO);
			return ResponseObject.success(responseDTO);

		} catch (CloudBaseException exp) {
			exp.printStackTrace();
			throw exp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

	public ResponseObject<List<CustomerPaymentSummaryDTO>> customerPendingPayments(String company, String branch) {
		
		List<CustomerPaymentSummary> customerPaymentSummaryList = customerPaymentSummaryRepository.getCustomerPendingPayments(company, branch);
		List<Long> paymentIds = customerPaymentSummaryList.stream().map(CustomerPaymentSummary::getId)
				.collect(Collectors.toList());
		List<CustomerPaymentDetails> detailsList = customerPaymentDetailsRepository.getDataBySummaryIds(paymentIds);
		
		List<CustomerPaymentSummaryDTO> customerPaymentSummaryDtoList = new ArrayList<>();
		
		
		if(!customerPaymentSummaryList.isEmpty()) {
			Map<Long, List<CustomerPaymentDetailsDTO>> custDetailsMap = detailsList.stream().map(detailsEntity -> {
				CustomerPaymentDetailsDTO detailsDto = new CustomerPaymentDetailsDTO();
				BeanUtils.copyProperties(detailsEntity, detailsDto);
				return detailsDto;
			}).collect(Collectors.groupingBy(CustomerPaymentDetailsDTO::getPaymentSummeryId));
			
			for(CustomerPaymentSummary entity : customerPaymentSummaryList) {
				CustomerPaymentSummaryDTO dto = new CustomerPaymentSummaryDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setCustomerPaymentDetails(custDetailsMap.getOrDefault(entity.getId(), new ArrayList<>()));
				customerPaymentSummaryDtoList.add(dto);
			}
		}
		
		return ResponseObject.success(customerPaymentSummaryDtoList);
	}

}
