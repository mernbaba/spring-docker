package com.profit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.profit.datamodel.Otp;

import jakarta.transaction.Transactional;

public interface OtpRepository extends JpaRepository<Otp, Long>, JpaSpecificationExecutor<Otp> {

	@Query(value = "select * from tb_otp where party_code=:userName and party_type=:userType and company_code=:companyCode ORDER BY createddate DESC LIMIT 1", nativeQuery = true)
	Otp findByPartyCodeAndPartyTypeAndCompanyCode(String userName, String userType, String companyCode);

	@Query(value = "update tb_otp set otp_status=:status where id=:id and party_code=:partyCode ", nativeQuery = true)
	@Transactional
	@Modifying
	void updateOtpStatusByIdAndPartyCode(String status, Long id, String partyCode);

}
