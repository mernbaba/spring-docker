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

//	@Scheduled(cron = "0 */2 * * * ?")
	@Scheduled(cron = "0 0 */2 * * ?")
	public synchronized void checkAllCustomersPtPlanRenewals() {

		System.err.println("Running PT Payment Summary schedular........");

		try {
			List<CustomerMaster> customerMasterList = customerMasterRepository.findAll();
//			List<CustomerMaster> customerMasterList = customerMasterRepository
//					.findAllByCompanyCodeAndBranchCode("COMP-21", "BRNC-21");

			List<String> customerCodes = customerMasterList.stream().map(CustomerMaster::getCustomerCode)
					.collect(Collectors.toList());

			List<PTPaymentSummary> ptSummeryList = ptPaymentSummaryRepository
					.getLatestPtSummaryRecordsOfCustomers(customerCodes, LocalDate.now());

			for (String code : customerCodes) {

				List<PTPaymentSummary> newList = ptSummeryList.stream().filter(pt -> pt.getCustomerCode().equals(code))
						.toList();
				if (!newList.isEmpty()) {
					updateLatestPtDatesInCustomerData(newList, code);
				}

			}

//			if (!ptSummeryList.isEmpty()) {
////				updateLatestPtDatesInCustomerData(ptSummeryList);
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Transactional
	private void updateLatestPtDatesInCustomerData(List<PTPaymentSummary> ptSummaryList, String custCode) {

		if (ptSummaryList != null) {
			Optional<CustomerMaster> customerOptional = customerMasterRepository.findByCustomerCode(custCode);

			if (customerOptional.isPresent()) {

				CustomerMaster customer = customerOptional.get();

				if (ptSummaryList.size() > 1) {
					PTPaymentSummary summ = ptSummaryList.stream()
							.max(Comparator.comparingLong(PTPaymentSummary::getId)).get();
					if (!summ.getPtStartDateOfPlan().equals(customer.getPtStartDateOfPlan())
							|| !summ.getPtEndDateOfPlan().equals(customer.getPtEndDateOfPlan())) {
						customer.setHasPT(true);
						customer.setStaffCode(summ.getStaffCode());
						customer.setStaffName(summ.getStaffName());
						customer.setPtStartDateOfPlan(summ.getPtStartDateOfPlan());
						customer.setPtEndDateOfPlan(summ.getPtEndDateOfPlan());
						customer.setPtPaymentPlan(summ.getPtPaymentPlan());
						customer.setLastModifiedBy("schedular");
						customerMasterRepository.save(customer);
					}

				} else {
					if (!ptSummaryList.get(0).getPtStartDateOfPlan().equals(customer.getPtStartDateOfPlan())
							|| !ptSummaryList.get(0).getPtEndDateOfPlan().equals(customer.getPtEndDateOfPlan())) {
						customer.setHasPT(true);
						customer.setStaffCode(ptSummaryList.get(0).getStaffCode());
						customer.setStaffName(ptSummaryList.get(0).getStaffName());
						customer.setPtStartDateOfPlan(ptSummaryList.get(0).getPtStartDateOfPlan());
						customer.setPtEndDateOfPlan(ptSummaryList.get(0).getPtEndDateOfPlan());
						customer.setPtPaymentPlan(ptSummaryList.get(0).getPtPaymentPlan());
						customer.setLastModifiedBy("schedular");
						customerMasterRepository.save(customer);
					}
				}

			}
		}
	}

}
