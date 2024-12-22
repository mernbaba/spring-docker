package com.profit.scheduler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.profit.datamodel.CustomerMaster;
import com.profit.datamodel.CustomerPaymentSummary;
import com.profit.datamodel.PTPaymentSummary;
import com.profit.repository.CustomerMasterRepository;
import com.profit.repository.PTPaymentSummaryRepository;

import jakarta.transaction.Transactional;

@Component
public class PTPaymentPlanRenewalSchedular {

	@Autowired
	CustomerMasterRepository customerMasterRepository;

	@Autowired
	PTPaymentSummaryRepository ptPaymentSummaryRepository;

//	@Scheduled(cron = "0 1 0 * * ?")
	@Scheduled(cron = "0 0 */2 * * ?")
	public synchronized void checkAllCustomersPtPlanRenewals() {

		System.err.println("Running PT Payment Summary schedular........");

		try {
//			List<String> customerCodes = customerMasterRepository.getRecordsByPTEndOfPlan(currentDate);
//
//			List<PTPaymentSummary> latestValidRecords = ptPaymentSummaryRepository
//					.findByCustomerCode(customerCodes).stream()
//					.collect(Collectors.groupingBy(PTPaymentSummary::getCustomerCode)).values().stream()
//					.map(group -> group.stream().max(Comparator.comparing(PTPaymentSummary::getPtStartDateOfPlan))
//							.orElse(null))
//					.filter(record -> record != null && (record.getPtStartDateOfPlan().isAfter(LocalDate.now())
//							|| record.getPtStartDateOfPlan().isEqual(LocalDate.now())))
//					.collect(Collectors.toList());
//
//			updateLatestPtDatesInCustomerData(latestValidRecords);
			List<CustomerMaster> customerMasterList = customerMasterRepository.findAll();
			
			List<String> customerCodes = customerMasterList.stream()
				    .map(CustomerMaster::getCustomerCode)
				    .collect(Collectors.toList());

			List<PTPaymentSummary> ptSummeryList = ptPaymentSummaryRepository
					.getLatestPtSummaryRecordsOfCustomers(customerCodes, LocalDate.now());

			if (!ptSummeryList.isEmpty()) {
				updateLatestPtDatesInCustomerData(ptSummeryList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Transactional
	private void updateLatestPtDatesInCustomerData(List<PTPaymentSummary> ptSummaryList) {

		if (ptSummaryList != null) {
			for (PTPaymentSummary ptRecord : ptSummaryList) {
				Optional<CustomerMaster> customerOptional = customerMasterRepository
						.findByCustomerCode(ptRecord.getCustomerCode());

				if (customerOptional.isPresent()) {

					CustomerMaster customer = customerOptional.get();
					
					if (ptSummaryList.size() > 1) {
						PTPaymentSummary summ = ptSummaryList.stream().max(Comparator.comparingLong(PTPaymentSummary::getId))
								.get();
						customer.setHasPT(true);
						customer.setStaffCode(summ.getStaffCode());
						customer.setStaffName(summ.getStaffName());
						customer.setPtStartDateOfPlan(summ.getPtStartDateOfPlan());
						customer.setPtEndDateOfPlan(summ.getPtEndDateOfPlan());
						customer.setPtPaymentPlan(summ.getPtPaymentPlan());
					}else {
						customer.setHasPT(true);
						customer.setStaffCode(ptRecord.getStaffCode());
						customer.setStaffName(ptRecord.getStaffName());
						customer.setPtStartDateOfPlan(ptRecord.getPtStartDateOfPlan());
						customer.setPtEndDateOfPlan(ptRecord.getPtEndDateOfPlan());
						customer.setPtPaymentPlan(ptRecord.getPtPaymentPlan());
					}

					customerMasterRepository.save(customer);
				}
			}
		}
	}

}
