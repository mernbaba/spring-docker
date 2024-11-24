package com.profit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.CustomerPaymentDetails;

@Repository
public interface CustomerPaymentDetailsRepository extends JpaRepository<CustomerPaymentDetails, Long>, JpaSpecificationExecutor<CustomerPaymentDetails>{

}
