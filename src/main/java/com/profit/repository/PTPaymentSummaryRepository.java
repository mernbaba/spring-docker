package com.profit.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.CustomerPaymentSummary;
import com.profit.datamodel.PTPaymentSummary;

@Repository
public interface PTPaymentSummaryRepository
		extends JpaRepository<PTPaymentSummary, Long>, JpaSpecificationExecutor<PTPaymentSummary> {

//	@Query(value = "SELECT * FROM tb_pt_payment_summary \n" + "WHERE company_code = :company \n"
//			+ "AND branch_code = :branch \n"
//			+ "AND ((pt_start_date_of_plan BETWEEN :fromDt AND :toDt OR pt_end_date_of_plan BETWEEN :fromDt AND :toDt)\n"
//			+ "OR (:fromDt BETWEEN pt_start_date_of_plan AND pt_end_date_of_plan OR :toDt BETWEEN pt_start_date_of_plan AND pt_end_date_of_plan))", nativeQuery = true)
//	List<PTPaymentSummary> getPtsByPlanDates(LocalDate fromDt, LocalDate toDt, String branch, String company);

	@Query(value = "SELECT * FROM tb_pt_payment_summary WHERE company_code = :company AND branch_code = :branch AND created_date BETWEEN :fromDt AND :toDt\r\n"
			+ "AND is_active = true", nativeQuery = true)
	List<PTPaymentSummary> getActivePtsByDate(@Param("fromDt") LocalDate fromDt, @Param("toDt") LocalDate toDt,
			@Param("branch") String branch, @Param("company") String company);

//	@Query(value = "SELECT * FROM tb_pt_payment_summary \n" + "WHERE company_code = :company \n"
//			+ "AND branch_code = :branch \n" + "AND customer_code = :customerCode\n"
//			+ "AND ((pt_start_date_of_plan BETWEEN :fromDt AND :toDt OR pt_end_date_of_plan BETWEEN :fromDt AND :toDt)\n"
//			+ "OR (:fromDt BETWEEN pt_start_date_of_plan AND pt_end_date_of_plan OR :toDt BETWEEN pt_start_date_of_plan AND pt_end_date_of_plan))", nativeQuery = true)
//	List<PTPaymentSummary> getPtsByPlanDatesWithCustomerCode(LocalDate fromDt, LocalDate toDt, String customerCode,
//			String branch, String company);

	@Query(value = "SELECT * FROM tb_pt_payment_summary\r\n" + "WHERE company_code = :company \r\n"
			+ "AND branch_code = :branch   \r\n" + "AND customer_code = :customerCode\r\n"
			+ "AND created_date BETWEEN :fromDt AND :toDt\r\n" + "AND is_active = true", nativeQuery = true)
	List<PTPaymentSummary> getActivePtsByDatesWithCustomerCode(@Param("fromDt") LocalDate fromDt,
			@Param("toDt") LocalDate toDt, @Param("customerCode") String customerCode, @Param("branch") String branch,
			@Param("company") String company);

	@Query(value = "SELECT * FROM tb_pt_payment_summary WHERE customer_code = :customerCode and company_code = :company and branch_code = :branch and is_active = true", nativeQuery = true)
	List<PTPaymentSummary> findByCustomerCodeAndCompanyCodeAndBranchCode(@Param("customerCode") String customerCode,
			@Param("company") String company, @Param("branch") String branch);

	@Query(value = "SELECT * FROM tb_pt_payment_summary WHERE customer_code IN (:customerCodes) and is_active = true", nativeQuery = true)
	List<PTPaymentSummary> findByCustomerCode(@Param("customerCodes") List<String> customerCodes);

//	@Query(value = "SELECT *  FROM tb_pt_payment_summary WHERE customer_code =:customerCode	ORDER BY pt_end_date_of_plan DESC LIMIT 1;", nativeQuery = true)
//	PTPaymentSummary getLatestRecordOfCustomer(@Param("customerCode") String customerCode);

	@Query(value = "SELECT *  FROM tb_pt_payment_summary WHERE is_active = true and customer_code =:customerCode"
			+ "AND (pt_start_date_of_plan =:localDate OR pt_end_date_of_plan=:localDate OR (:localDate BETWEEN pt_start_date_of_plan AND pt_end_date_of_plan))", nativeQuery = true)
	List<PTPaymentSummary> getLatestPtSummaryOfCustomer(@Param("customerCode") String customerCode,
			LocalDate localDate);

	@Query(value = "SELECT *  FROM tb_pt_payment_summary WHERE is_active = true and customer_code IN :customerCodes "
			+ "AND (pt_start_date_of_plan =:localDate " + "OR pt_end_date_of_plan=:localDate "
			+ "OR (:localDate BETWEEN pt_start_date_of_plan AND pt_end_date_of_plan))", nativeQuery = true)
	List<PTPaymentSummary> getLatestPtSummaryRecordsOfCustomers(@Param("customerCodes") List<String> customerCodes,
			LocalDate localDate);

	@Query(value = "SELECT * FROM tb_pt_payment_summary \r\n"
			+ "WHERE (pt_start_date_of_plan BETWEEN :startDate AND :endDate\r\n"
			+ "OR pt_end_date_of_plan BETWEEN :startDate  AND :endDate)\r\n"
			+ "AND company_code = :company\r\n"
			+ "AND branch_code = :branch \r\n"
			+ "AND is_active = true", nativeQuery = true)
	List<PTPaymentSummary> getCustomerPtPendingPayments(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate, @Param("company") String company, @Param("branch") String branch);
}
