package com.profit.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.CustomerPaymentSummary;
import com.profit.dto.CustomerPaymentSummaryDTO;

@Repository
public interface CustomerPaymentSummaryRepository
		extends JpaRepository<CustomerPaymentSummary, Long>, JpaSpecificationExecutor<CustomerPaymentSummary> {

	List<CustomerPaymentSummary> findAllByBranchCodeAndCompanyCodeAndCustomerCode(String branch, String company,
			String userCode);

	@Query(value = "SELECT * FROM tb_customer_payment_summary\r\n" 
			+ "WHERE company_code = :company \r\n"
			+ "AND branch_code = :branch \r\n"
			+ "AND ((plan_start_date BETWEEN :fromDt AND :toDt OR plan_end_date BETWEEN :fromDt AND :toDt)\r\n"
			+ "OR (:fromDt BETWEEN plan_start_date AND plan_end_date OR :toDt BETWEEN plan_start_date AND plan_end_date))", nativeQuery = true)
	List<CustomerPaymentSummary> getCustomersByPlanDates(@Param("fromDt") LocalDate fromDt,
			@Param("toDt") LocalDate toDt, @Param("branch") String branch, @Param("company") String company);

	@Query(value = "SELECT * FROM tb_customer_payment_summary\r\n"
			+ "WHERE company_code = :company \r\n"
			+ "AND branch_code = :branch \r\n"
			+ "AND customer_code = :customerCode\r\n"
			+ "AND ((plan_start_date BETWEEN :fromDt AND :toDt OR plan_end_date BETWEEN :fromDt AND :toDt)\r\n"
			+ "OR (:fromDt BETWEEN plan_start_date AND plan_end_date OR :toDt BETWEEN plan_start_date AND plan_end_date))", nativeQuery = true)
	List<CustomerPaymentSummary> getCustomersByPlanDatesWithCustomer(@Param("fromDt") LocalDate fromDt,
			@Param("toDt") LocalDate toDt,@Param("customerCode") String customerCode, @Param("branch") String branch, @Param("company") String company);

	@Query(value = "SELECT * FROM tb_customer_payment_summary WHERE customer_code IN (:customerCodes)", nativeQuery = true)
	List<CustomerPaymentSummary> findByCustomerCode(@Param("customerCodes") List<String> customerCodes);

}
