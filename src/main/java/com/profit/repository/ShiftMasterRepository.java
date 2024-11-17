package com.profit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.ShiftMaster;

@Repository
public interface ShiftMasterRepository extends JpaRepository<ShiftMaster,Long>,JpaSpecificationExecutor<ShiftMaster> {

	List<ShiftMaster> findAllByCompanyCodeAndBranchCode(String company, String branch);

}
