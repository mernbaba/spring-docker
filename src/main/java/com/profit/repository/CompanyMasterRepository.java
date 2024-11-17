package com.profit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.CompanyMaster;

@Repository
public interface CompanyMasterRepository  extends JpaRepository<CompanyMaster,Long>,JpaSpecificationExecutor<CompanyMaster> {

}
