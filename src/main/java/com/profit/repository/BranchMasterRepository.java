package com.profit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.BranchMaster;

@Repository
public interface BranchMasterRepository  extends JpaRepository<BranchMaster,Long>,JpaSpecificationExecutor<BranchMaster> {

	BranchMaster findByBranchCodeAndCompanyCode(String branch, String company);

}