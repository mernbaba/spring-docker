package com.profit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.RoleMaster;

@Repository
public interface RoleMasterRepository extends JpaRepository<RoleMaster,Long>,JpaSpecificationExecutor<RoleMaster> {

}
