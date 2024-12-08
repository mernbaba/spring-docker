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

	@Scheduled(cron = "0 30 18 * * ?")
//		@Scheduled(cron = "0 */2 * * * ?")
	public synchronized void checkAllCustomersPtPlanRenewals() {

		System.err.println("Running PT Payment Summary schedular........");

		try {
			List<String> customerCodes = customerMasterRepository.getRecordsByPTEndOfPlan();

			List<PTPaymentSummary> latestValidRecords = ptPaymentSummaryRepository
					.findByCustomerCode(customerCodes).stream()
					.collect(Collectors.groupingBy(PTPaymentSummary::getCustomerCode)).values().stream()
					.map(group -> group.stream().max(Comparator.comparing(PTPaymentSummary::getPtStartDateOfPlan))
							.orElse(null))
					.filter(record -> record != null && (record.getPtStartDateOfPlan().isAfter(LocalDate.now())
							|| record.getPtStartDateOfPlan().isEqual(LocalDate.now())))
					.collect(Collectors.toList());

			updateLatestPtDatesInCustomerData(latestValidRecords);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Transactional
	private void updateLatestPtDatesInCustomerData(List<PTPaymentSummary> latestValidRecords) {

		if (latestValidRecords != null) {
			for (PTPaymentSummary ptRecord : latestValidRecords) {
				Optional<CustomerMaster> customerOptional = customerMasterRepository
						.findByCustomerCode(ptRecord.getCustomerCode());

				if (customerOptional.isPresent()) {

					CustomerMaster customer = customerOptional.get();

					customer.setHasPT(true);
					customer.setStaffCode(ptRecord.getStaffCode());
					customer.setStaffName(ptRecord.getStaffName());
					customer.setPtStartDateOfPlan(ptRecord.getPtStartDateOfPlan());
					customer.setPtEndDateOfPlan(ptRecord.getPtEndDateOfPlan());
					customer.setPtPaymentPlan(ptRecord.getPtPaymentPlan());

					customerMasterRepository.save(customer);
				}
			}
		}
	}

}
