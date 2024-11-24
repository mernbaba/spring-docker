package com.profit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.PTPricingPlans;

@Repository
public interface PTPricingPlanRepository extends JpaRepository<PTPricingPlans, Long>, JpaSpecificationExecutor<PTPricingPlans>{

	List<PTPricingPlans> findAllByCompanyCodeAndBranchCode(String company, String branch);
	
}
