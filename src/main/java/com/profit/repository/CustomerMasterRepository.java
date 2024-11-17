package com.profit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.CustomerMaster;

@Repository
public interface CustomerMasterRepository extends JpaRepository<CustomerMaster,Long>,JpaSpecificationExecutor<CustomerMaster>{

	CustomerMaster findByPhoneNumber(String phone);

	List<CustomerMaster> findAllByCompanyCodeAndBranchCode(String company, String branch);

}
