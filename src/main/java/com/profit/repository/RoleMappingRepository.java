package com.profit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.RoleMapping;

@Repository
public interface RoleMappingRepository extends JpaRepository<RoleMapping,Long>,JpaSpecificationExecutor<RoleMapping> {

	List<RoleMapping> findAllByCompanyCodeAndBranchCode(String company, String branch);

	List<RoleMapping> findByUserCodeAndCompanyCodeAndBranchCode(String usercode, String company, String branch);

}
