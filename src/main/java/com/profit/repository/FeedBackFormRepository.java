package com.profit.repository;

import com.profit.datamodel.FeedBackForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBackFormRepository extends JpaRepository<FeedBackForm, Long>, JpaSpecificationExecutor<FeedBackForm> {

    List<FeedBackForm> findAllByCompanyCodeAndBranchCode(String company, String branch);

    FeedBackForm findByUserCode(String userCode);
}
