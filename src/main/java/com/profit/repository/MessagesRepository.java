package com.profit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.profit.datamodel.Messages;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long>, JpaSpecificationExecutor<Messages> {

}
