package com.profit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.SecUser;

import jakarta.transaction.Transactional;

@Repository
public interface SecUserRepository extends JpaRepository<SecUser, Long>, JpaSpecificationExecutor<SecUser> {

	SecUser findByUserName(String username);

	SecUser findByUserCode(String userCode);

	List<SecUser> findAllByCompanyCodeAndBranchCode(String company, String branch);

	@Modifying
	@Transactional
	@Query(value = "update sec_user set password=:newPwd , last_modified_by=:userName where user_name=:userName and  company_code=:company and branch_code=:branch ", nativeQuery = true)
	void updatePassword(String userName, String company, String branch, String newPwd);

	SecUser findByEmail(String email);

}
