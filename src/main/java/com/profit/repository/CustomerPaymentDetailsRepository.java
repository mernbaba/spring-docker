package com.profit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.CustomerPaymentDetails;

@Repository
public interface CustomerPaymentDetailsRepository
		extends JpaRepository<CustomerPaymentDetails, Long>, JpaSpecificationExecutor<CustomerPaymentDetails> {

	@Query(value = "select * from tb_customer_payment_details where payment_summery_id IN :paymentIds", nativeQuery = true)
	List<CustomerPaymentDetails> getDataBySummaryIds(List<Long> paymentIds);

}
