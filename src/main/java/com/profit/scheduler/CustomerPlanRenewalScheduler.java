package com.profit.scheduler;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.profit.datamodel.CustomerMaster;
import com.profit.datamodel.CustomerPaymentSummary;
import com.profit.repository.CustomerMasterRepository;
import com.profit.repository.CustomerPaymentSummaryRepository;

import jakarta.transaction.Transactional;

@Component
public class CustomerPlanRenewalScheduler {

	@Autowired
	CustomerMasterRepository customerMasterRepository;

	@Autowired
	CustomerPaymentSummaryRepository customerPaymentSummaryRepository;

//	@Scheduled(cron = "0 1 0 * * ?")
	@Scheduled(cron = "0 0 */2 * * ?")
	public synchronized void checkAllCustomersPlanRenewals() {
		
		System.err.println("Running customer Payment Summary schedular........");

		try {
			//get all customers
			List<CustomerMaster> customerMasterList = customerMasterRepository.findAll();
			
			List<String> customerCodes = customerMasterList.stream()
				    .map(CustomerMaster::getCustomerCode)
				    .collect(Collectors.toList());
			
				
			List<CustomerPaymentSummary> summaryList = customerPaymentSummaryRepository
						.getLatestSummaryRecordsOfCustomers(customerCodes, LocalDate.now());
				
			if (!summaryList.isEmpty()) {
				updateLatestDatesInCustomerData(summaryList);
			}
			
			
//			if (summaryList.size() > 0 && !summaryList.isEmpty()) {
//
//				Optional<CustomerMaster> master = customerMasterRepository.findByCustomerCode(entity.getCustomerCode());
//				if (master.isPresent()) {
//					CustomerMaster cust = master.get();
//
//					if (summaryList.size() > 1) {
//						CustomerPaymentSummary summ = summaryList.stream()
//								.max(Comparator.comparingLong(CustomerPaymentSummary::getId)).get();
//
//						cust.setStartDateOfPlan(summ.getPlanStartDate());
//						cust.setEndDateOfPlan(summ.getPlanEndDate());
//						cust.setPaymentPlan(summ.getPaymentPlan());
//
//					} else {
//						cust.setStartDateOfPlan(summaryList.get(0).getPlanStartDate());
//						cust.setEndDateOfPlan(summaryList.get(0).getPlanEndDate());
//						cust.setPaymentPlan(summaryList.get(0).getPaymentPlan());
//					}
//					customerMasterRepository.save(cust);
//
//				}
//
//			}

//			List<CustomerPaymentSummary> latestValidRecords = customerPaymentSummaryRepository
//					.findByCustomerCode(customerCodes).stream()
//					.collect(Collectors.groupingBy(CustomerPaymentSummary::getCustomerCode)).values().stream()
//					.map(group -> group.stream().max(Comparator.comparing(CustomerPaymentSummary::getPlanStartDate))
//							.orElse(null))
//					.filter(record -> record != null && (record.getPlanStartDate().isAfter(LocalDate.now())
//							|| record.getPlanStartDate().isEqual(LocalDate.now())))
//					.collect(Collectors.toList());
//
//			updateLatestDatesInCustomerData(latestValidRecords);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Transactional
	private void updateLatestDatesInCustomerData(List<CustomerPaymentSummary> latestValidRecords) {
		
		if (latestValidRecords != null) {
			for (CustomerPaymentSummary custRecord : latestValidRecords) {
				Optional<CustomerMaster> customerOptional = customerMasterRepository
						.findByCustomerCode(custRecord.getCustomerCode());

				if (customerOptional.isPresent()) {
					CustomerMaster customer = customerOptional.get();
					
					if (latestValidRecords.size() > 1) {
						CustomerPaymentSummary summ = latestValidRecords.stream()
								.max(Comparator.comparingLong(CustomerPaymentSummary::getId)).get();
						
						customer.setStartDateOfPlan(summ.getPlanStartDate());
						customer.setEndDateOfPlan(summ.getPlanEndDate());
						customer.setPaymentPlan(summ.getPaymentPlan());
					}else {
						customer.setStartDateOfPlan(custRecord.getPlanStartDate());
						customer.setEndDateOfPlan(custRecord.getPlanEndDate());
						customer.setPaymentPlan(custRecord.getPaymentPlan());
					}

					customerMasterRepository.save(customer);
				}
			}
		}
	}

}
