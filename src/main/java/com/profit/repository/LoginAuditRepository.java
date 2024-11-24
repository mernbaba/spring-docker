package com.profit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.LoginAudit;

@Repository
public interface LoginAuditRepository extends JpaRepository<LoginAudit, Long>, JpaSpecificationExecutor<LoginAudit> {

}
