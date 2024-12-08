package com.profit.scheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.profit.datamodel.CustomerMaster;
import com.profit.repository.CustomerMasterRepository;

@Component
public class PTSubscriptionValidationScheduler {
	
	@Autowired
	CustomerMasterRepository customerMasterRepository;
	
//	@Scheduled(cron = "0 30 17 * * ?")
	public synchronized void checkAllCustomersPtPlans() { 
		
		System.err.println("Checking if customers has active PT plan........");
		
		try {
			
			List<CustomerMaster> customerMasterList = customerMasterRepository.findAllByIsActiveAndHasPt().stream()
					.filter(customer -> customer.getPtEndDateOfPlan() != null
							&& customer.getPtEndDateOfPlan().isBefore(LocalDate.now())).collect(Collectors.toList());
			
			for(CustomerMaster master : customerMasterList) {
				master.setHasPT(false);
				master.setStaffCode(null);
				master.setStaffName(null);
				master.setPtStartDateOfPlan(null);
				master.setPtEndDateOfPlan(null);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	

}
