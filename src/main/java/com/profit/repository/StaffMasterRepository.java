package com.profit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.StaffMaster;

@Repository
public interface StaffMasterRepository extends JpaRepository<StaffMaster,Long>,JpaSpecificationExecutor<StaffMaster>{

	StaffMaster findByPhone(String phoneNumber);

	List<StaffMaster> findAllByCompanyCodeAndBranchCode(String company, String branch);

}