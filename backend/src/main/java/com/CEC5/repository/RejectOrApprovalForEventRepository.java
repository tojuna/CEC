package com.CEC5.repository;

import com.CEC5.entity.RejectOrApprovalForEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface RejectOrApprovalForEventRepository extends JpaRepository<RejectOrApprovalForEvent, Long>, QuerydslPredicateExecutor<RejectOrApprovalForEvent> {
}
