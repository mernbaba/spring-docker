package com.profit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.PTPaymentDetails;

@Repository
public interface PTPaymentDetailsRepository
		extends JpaRepository<PTPaymentDetails, Long>, JpaSpecificationExecutor<PTPaymentDetails> {

	@Query(value = "select * from tb_pt_payment_details where pt_payment_summery_id IN :paymentIds", nativeQuery = true)
	List<PTPaymentDetails> getDataBySummaryIds(List<Long> paymentIds);

}
