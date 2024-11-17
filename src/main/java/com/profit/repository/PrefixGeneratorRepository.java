package com.profit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.PrefixGenerator;

@Repository
public interface PrefixGeneratorRepository extends JpaRepository<PrefixGenerator,Long>,JpaSpecificationExecutor<PrefixGenerator>{

	Optional<PrefixGenerator> findByCompanyCode(String companyCode);

	PrefixGenerator findByBranchCodeAndCompanyCode(String branch, String company);
	
//	@Query(value = "select * from prefix_generator ORDER BY pref_code_id DESC LIMIT 1", nativeQuery = true)
//	PrefixGenerator getLastCreatedRecord();
	
	PrefixGenerator findByBranchCodeAndCompanyCodeAndPrefixCode(String branch, String company, String prefixCode);

}
