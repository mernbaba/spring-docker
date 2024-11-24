package com.profit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.PTPaymentDetails;

@Repository
public interface PTPaymentDetailsRepository extends JpaRepository<PTPaymentDetails, Long>, JpaSpecificationExecutor<PTPaymentDetails>{

}
