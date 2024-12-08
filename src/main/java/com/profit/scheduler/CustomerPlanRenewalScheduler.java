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

	@Scheduled(cron = "0 0 18 * * ?")
//	@Scheduled(cron = "0 2 * * * ?")
	public synchronized void checkAllCustomersPlanRenewals() {
		
		System.err.println("Running customer Payment Summary schedular........");

		try {
			List<String> customerCodes = customerMasterRepository.getRecordsByEndDateOfPlan();

			List<CustomerPaymentSummary> latestValidRecords = customerPaymentSummaryRepository
					.findByCustomerCode(customerCodes).stream()
					.collect(Collectors.groupingBy(CustomerPaymentSummary::getCustomerCode)).values().stream()
					.map(group -> group.stream().max(Comparator.comparing(CustomerPaymentSummary::getPlanStartDate))
							.orElse(null))
					.filter(record -> record != null && (record.getPlanStartDate().isAfter(LocalDate.now())
							|| record.getPlanStartDate().isEqual(LocalDate.now())))
					.collect(Collectors.toList());

			updateLatestDatesInCustomerData(latestValidRecords);
			

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

					customer.setStartDateOfPlan(custRecord.getPlanStartDate());
					customer.setEndDateOfPlan(custRecord.getPlanEndDate());
					customer.setPaymentPlan(custRecord.getPaymentPlan());

					customerMasterRepository.save(customer);
				}
			}
		}
	}

}
