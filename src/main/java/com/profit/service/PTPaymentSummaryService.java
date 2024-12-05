package com.profit.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.profit.datamodel.CustomerMaster;
import com.profit.datamodel.PTPaymentDetails;
import com.profit.datamodel.PTPaymentSummary;
import com.profit.dto.PTPaymentDetailsDTO;
import com.profit.dto.PTPaymentSummaryDTO;
import com.profit.dto.ResponseObject;
import com.profit.enumeration.ResponseCode;
import com.profit.exceptions.CloudBaseException;
import com.profit.repository.CustomerMasterRepository;
import com.profit.repository.PTPaymentDetailsRepository;
import com.profit.repository.PTPaymentSummaryRepository;

import jakarta.transaction.Transactional;

@Service
public class PTPaymentSummaryService {

	@Autowired
	PTPaymentSummaryRepository ptPaymentSummaryRepository;

	@Autowired
	CustomerMasterRepository customerMasterRepository;

	@Autowired
	PTPaymentDetailsRepository ptPaymentDetailsRepository;

	public ResponseObject<List<PTPaymentSummaryDTO>> getAllByDates(String fromDate, String toDate, String customerCode,
			String branch, String company) {

		try {

			List<PTPaymentSummaryDTO> dtoList = new ArrayList<>();
			List<PTPaymentSummary> paymentEntityList = new ArrayList<>();

			if (!StringUtils.isBlank(fromDate) && !StringUtils.isBlank(toDate)) {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

				LocalDate fromDt = LocalDate.parse(fromDate, format);
				LocalDate toDt = LocalDate.parse(toDate, format);

				if (customerCode.equalsIgnoreCase("ALL")) {

					paymentEntityList = ptPaymentSummaryRepository.getPtsByPlanDates(fromDt, toDt, branch, company);
				} else {
					paymentEntityList = ptPaymentSummaryRepository.getPtsByPlanDatesWithCustomerCode(fromDt, toDt,
							customerCode, branch, company);
				}
			} else {
				paymentEntityList = ptPaymentSummaryRepository.findByCustomerCode(customerCode);
			}
			List<Long> paymentIds = paymentEntityList.stream().map(PTPaymentSummary::getId)
					.collect(Collectors.toList());

			List<PTPaymentDetails> detailsList = ptPaymentDetailsRepository.getDataBySummaryIds(paymentIds);

			Map<Long, List<PTPaymentDetailsDTO>> detailsMap = detailsList.stream().map(detailsEntity -> {
				PTPaymentDetailsDTO detailsDto = new PTPaymentDetailsDTO();
				BeanUtils.copyProperties(detailsEntity, detailsDto);
				return detailsDto;
			}).collect(Collectors.groupingBy(PTPaymentDetailsDTO::getPtPaymentSummeryId));

			if (paymentEntityList != null) {
				paymentEntityList.forEach(entity -> {
					PTPaymentSummaryDTO dto = new PTPaymentSummaryDTO();
					BeanUtils.copyProperties(entity, dto);
					List<PTPaymentDetailsDTO> paymentDetailsList = detailsMap.getOrDefault(entity.getId(),
							new ArrayList<>());
					dto.setPtPaymentDetails(paymentDetailsList);
					dtoList.add(dto);
				});
			}
			return ResponseObject.success(dtoList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.INVALID_QUERY);
		}

	}

	@Transactional
	public ResponseObject<PTPaymentSummaryDTO> save(PTPaymentSummaryDTO dto, String branch, String company,
			String username) {

		try {

			PTPaymentSummary entity = new PTPaymentSummary();
			BeanUtils.copyProperties(dto, entity);

			entity.setCompanyCode(company);
			entity.setBranchCode(branch);
			entity.setCreatedBy(username);

			CustomerMaster customerMaster = customerMasterRepository.findByCustomerCode(dto.getCustomerCode()).get();

			if (customerMaster.getPtEndDateOfPlan() == null
					|| dto.getPtStartDateOfPlan().isAfter(customerMaster.getPtEndDateOfPlan())) {

				customerMaster.setHasPT(true);
				customerMaster.setStaffCode(dto.getStaffCode());
				customerMaster.setStaffName(dto.getStaffName());
				customerMaster.setPtStartDateOfPlan(dto.getPtStartDateOfPlan());
				customerMaster.setPtEndDateOfPlan(dto.getPtEndDateOfPlan());
				customerMaster.setPtPaymentPlan(dto.getPtPaymentPlan());
				customerMaster.setLastModifiedBy(username);

				customerMasterRepository.save(customerMaster);

			}

			Long id = ptPaymentSummaryRepository.save(entity).getId();

			List<PTPaymentDetails> detailtsEntityList = new ArrayList<>();
			if (dto.getPtPaymentDetails() != null && !dto.getPtPaymentDetails().isEmpty()) {
				dto.getPtPaymentDetails().forEach(details -> {
					PTPaymentDetails detailsEnt = new PTPaymentDetails();
					BeanUtils.copyProperties(details, detailsEnt);
					detailsEnt.setPtPaymentSummeryId(id);
					detailsEnt.setCreatedBy(username);
					detailtsEntityList.add(detailsEnt);
				});

				ptPaymentDetailsRepository.saveAll(detailtsEntityList);
			}

			PTPaymentSummaryDTO responseDTO = new PTPaymentSummaryDTO();
			BeanUtils.copyProperties(entity, responseDTO);
			return ResponseObject.success(responseDTO);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

	@Transactional
	public ResponseObject<PTPaymentSummaryDTO> update(PTPaymentSummaryDTO dto, String branch, String company,
			String username) {

		try {

			PTPaymentSummary entity = ptPaymentSummaryRepository.findById(dto.getId())
					.orElseThrow(() -> new CloudBaseException(ResponseCode.USER_NOT_FOUND));

			BeanUtils.copyProperties(dto, entity);

			entity.setLastModifiedBy(username);

			Long id = ptPaymentSummaryRepository.save(entity).getId();

			List<PTPaymentDetails> detailsEntityList = new ArrayList<>();
			if (dto.getPtPaymentDetails() != null) {
				dto.getPtPaymentDetails().forEach(details -> {
					PTPaymentDetails detailsEnt = new PTPaymentDetails();
					BeanUtils.copyProperties(details, detailsEnt);
					if (detailsEnt.getId() == null) {
						detailsEnt.setPtPaymentSummeryId(id);
						detailsEnt.setCreatedBy(username);

					} else {
						detailsEnt.setLastModifiedBy(username);
					}
					detailsEntityList.add(detailsEnt);
				});
				ptPaymentDetailsRepository.saveAll(detailsEntityList);
			}

			PTPaymentSummaryDTO responseDTO = new PTPaymentSummaryDTO();
			BeanUtils.copyProperties(entity, responseDTO);
			return ResponseObject.success(responseDTO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

}
