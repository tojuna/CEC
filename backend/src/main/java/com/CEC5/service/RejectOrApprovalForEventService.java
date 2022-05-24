package com.CEC5.service;

import com.CEC5.entity.QRejectOrApprovalForEvent;
import com.CEC5.entity.RejectOrApprovalForEvent;
import com.CEC5.repository.RejectOrApprovalForEventRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RejectOrApprovalForEventService {

    @Autowired
    RejectOrApprovalForEventRepository repository;

    public void save(RejectOrApprovalForEvent rejectOrApprovalForEvent) {
        repository.save(rejectOrApprovalForEvent);
    }

    public Long numberOfRejectsOrApprovals(String email) {
        QRejectOrApprovalForEvent qRejectOrApprovalForEvent = QRejectOrApprovalForEvent.rejectOrApprovalForEvent;
        BooleanExpression c = qRejectOrApprovalForEvent.userOnWhoActionIsApplied.email.eq(email);
        return repository.count(c);
    }
}
