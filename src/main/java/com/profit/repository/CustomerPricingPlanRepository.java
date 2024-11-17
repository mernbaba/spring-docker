package com.profit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.CustomerPricingPlan;

@Repository
public interface CustomerPricingPlanRepository extends JpaRepository<CustomerPricingPlan,Long>,JpaSpecificationExecutor<CustomerPricingPlan> {

	List<CustomerPricingPlan> findAllByCompanyCodeAndBranchCode(String company, String branch);

}
