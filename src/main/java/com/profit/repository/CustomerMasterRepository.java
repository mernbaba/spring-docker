package com.profit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.CustomerMaster;

import jakarta.transaction.Transactional;

@Repository
public interface CustomerMasterRepository extends JpaRepository<CustomerMaster,Long>,JpaSpecificationExecutor<CustomerMaster>{

	CustomerMaster findByPhoneNumber(String phone);

	List<CustomerMaster> findAllByCompanyCodeAndBranchCode(String company, String branch);

	@Transactional
	@Modifying
	@Query(value = "UPDATE customer_master SET is_active = false, last_modified_by = :userName WHERE customer_code = :userCode", nativeQuery = true)
	void updateCustomer(@Param("userCode") String userCode, @Param("userName") String username);

	@Query(value = "SELECT * from customer_master WHERE is_active = true", nativeQuery = true)
	List<CustomerMaster> findAllByIsActive();

	Optional<CustomerMaster> findByCustomerCode(String customerCode);

}
