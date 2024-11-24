package com.profit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.PTPaymentSummary;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PTPaymentSummaryRepository extends JpaRepository<PTPaymentSummary, Long>, JpaSpecificationExecutor<PTPaymentSummary>{

    @Query(value = "SELECT * FROM tb_pt_payment_summary \n" +
            "WHERE company_code = :company \n" +
            "AND branch_code = :branch \n" +
            "AND ((pt_start_date_of_plan BETWEEN :fromDt AND :toDt OR pt_end_date_of_plan BETWEEN :fromDt AND :toDt)\n" +
            "OR (:fromDt BETWEEN pt_start_date_of_plan AND pt_end_date_of_plan OR :toDt BETWEEN pt_start_date_of_plan AND pt_end_date_of_plan))", nativeQuery = true)
    List<PTPaymentSummary> getPtsByPlanDates(LocalDate fromDt, LocalDate toDt, String branch, String company);

    @Query(value = "SELECT * FROM tb_pt_payment_summary \n" +
            "WHERE company_code = :company \n" +
            "AND branch_code = :branch \n" +
            "AND customer_code = :customerCode\n" +
            "AND ((pt_start_date_of_plan BETWEEN :fromDt AND :toDt OR pt_end_date_of_plan BETWEEN :fromDt AND :toDt)\n" +
            "OR (:fromDt BETWEEN pt_start_date_of_plan AND pt_end_date_of_plan OR :toDt BETWEEN pt_start_date_of_plan AND pt_end_date_of_plan))", nativeQuery = true)
    List<PTPaymentSummary> getPtsByPlanDatesWithCustomerCode(LocalDate fromDt, LocalDate toDt, String customerCode, String branch, String company);
}
