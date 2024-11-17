package com.profit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.StaffTypeMaster;

@Repository
public interface StaffTypeMasterRepository
		extends JpaRepository<StaffTypeMaster, Long>, JpaSpecificationExecutor<StaffTypeMaster> {

	List<StaffTypeMaster> findByCompanyCodeAndBranchCode(String company, String branch);

}
