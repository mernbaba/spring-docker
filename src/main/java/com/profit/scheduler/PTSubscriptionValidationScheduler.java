package com.profit.scheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.profit.datamodel.CustomerMaster;
import com.profit.repository.CustomerMasterRepository;

import jakarta.transaction.Transactional;

@Component
public class PTSubscriptionValidationScheduler {

	@Autowired
	CustomerMasterRepository customerMasterRepository;

	@Scheduled(cron = "0 30 0 * * ?")
	@Transactional
	public synchronized void checkAllCustomersPtPlans() {

		System.err.println("Validating if customer has active PT plan........");

		try {

			List<CustomerMaster> customerMasterList = customerMasterRepository
					.findAllByIsActiveAndHasPtAndPtEndDate(LocalDate.now());

			if (!customerMasterList.isEmpty()) {
				for (CustomerMaster master : customerMasterList) {

					master.setHasPT(false);
					master.setStaffCode(null);
					master.setStaffName(null);
					master.setPtPaymentPlan(null);
					master.setLastModifiedBy("schedular");

					customerMasterRepository.save(master);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

}
