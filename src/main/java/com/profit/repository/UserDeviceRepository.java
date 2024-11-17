package com.profit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.UserDevice;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice,Long>,JpaSpecificationExecutor<UserDevice>{

	List<UserDevice> findAllByCompanyCodeAndBranchCode(String company, String branch);
	UserDevice findByUserCode(String userCode);
	
	@Query(value = "select * from tb_user_device where user_code = :userCode ORDER BY created_date DESC LIMIT 1", nativeQuery = true)
	UserDevice getLatestUserCode(@Param(value = "userCode") String userCode);

}
