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

//	@Scheduled(cron = "0 */2 * * * ?")
	@Scheduled(cron = "0 0 */2 * * ?")
	public synchronized void checkAllCustomersPlanRenewals() {

		System.err.println("Running customer Payment Summary schedular........");

		try {
			// get all customers
			List<CustomerMaster> customerMasterList = customerMasterRepository.findAll();
//			List<CustomerMaster> customerMasterList = customerMasterRepository
//					.findAllByCompanyCodeAndBranchCode("COMP-21", "BRNC-21");

			List<String> customerCodes = customerMasterList.stream().map(CustomerMaster::getCustomerCode)
					.collect(Collectors.toList());

			List<CustomerPaymentSummary> summaryList = customerPaymentSummaryRepository
					.getLatestSummaryRecordsOfCustomers(customerCodes, LocalDate.now());

			for (String code : customerCodes) {
				List<CustomerPaymentSummary> custPayments = summaryList.stream()
						.filter(cps -> cps.getCustomerCode().equals(code)).toList();
				if (!custPayments.isEmpty()) {
					updateLatestDatesInCustomerData(custPayments, code);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Transactional
	private void updateLatestDatesInCustomerData(List<CustomerPaymentSummary> latestValidRecords, String customerCode) {

		if (latestValidRecords != null) {
//			for (CustomerPaymentSummary custRecord : latestValidRecords) {
			Optional<CustomerMaster> customerOptional = customerMasterRepository.findByCustomerCode(customerCode);

			if (customerOptional.isPresent()) {
				CustomerMaster customer = customerOptional.get();

				if (latestValidRecords.size() > 1) {
					CustomerPaymentSummary summ = latestValidRecords.stream()
							.max(Comparator.comparingLong(CustomerPaymentSummary::getId)).get();
					if (!summ.getPlanStartDate().equals(customer.getStartDateOfPlan())
							|| !summ.getPlanEndDate().equals(customer.getEndDateOfPlan())) {

						customer.setStartDateOfPlan(summ.getPlanStartDate());
						customer.setEndDateOfPlan(summ.getPlanEndDate());
						customer.setPaymentPlan(summ.getPaymentPlan());
						customer.setLastModifiedBy("schedular");
						customerMasterRepository.save(customer);
					}
				} else {
					if (!latestValidRecords.get(0).getPlanStartDate().equals(customer.getStartDateOfPlan())
							|| !latestValidRecords.get(0).getPlanEndDate().equals(customer.getEndDateOfPlan())) {
						customer.setStartDateOfPlan(latestValidRecords.get(0).getPlanStartDate());
						customer.setEndDateOfPlan(latestValidRecords.get(0).getPlanEndDate());
						customer.setPaymentPlan(latestValidRecords.get(0).getPaymentPlan());
						customer.setLastModifiedBy("schedular");
						customerMasterRepository.save(customer);
					}
				}

			}
//			}
		}
	}

}
