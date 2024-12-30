package com.profit.repository;

import java.time.LocalDate;
import java.util.Collection;
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

	@Query(value = "SELECT customer_code  from customer_master WHERE is_active = true\r\n"
			+ "AND end_date_of_plan < :currentDate", nativeQuery = true)
	List<String> getRecordsByEndDateOfPlan(@Param("currentDate") LocalDate currentDate);
	
	@Query(value = "SELECT customer_code from customer_master WHERE is_active = true\r\n"
			+ "AND pt_end_date_of_plan < :currentDate\r\n"
			+ "OR pt_end_date_of_plan is NULL", nativeQuery = true)
	List<String> getRecordsByPTEndOfPlan(@Param("currentDate") LocalDate currentDate);

	Optional<CustomerMaster> findByCustomerCode(String customerCode);

	@Query(value = "SELECT * from customer_master cm\r\n"
			+ "WHERE is_active = true \r\n"
			+ "AND has_pt = true\r\n"
			+ "AND pt_end_date_of_plan < :currentDate", nativeQuery = true)
	List<CustomerMaster> findAllByIsActiveAndHasPtAndPtEndDate(@Param("currentDate") LocalDate currentDate);

}
