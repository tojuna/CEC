package com.CEC5.repository;

import com.CEC5.entity.Reviews;
import com.CEC5.entity.SignUpEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SignUpEventRepository extends JpaRepository<SignUpEvent, Long>, QuerydslPredicateExecutor<SignUpEvent> {
}
