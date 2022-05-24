package com.CEC5.service;

import com.CEC5.entity.QSignUpEvent;
import com.CEC5.entity.SignUpEvent;
import com.CEC5.repository.SignUpEventRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpEventService {

    @Autowired
    SignUpEventRepository repository;

    public void save(SignUpEvent signUpEvent) {
        repository.save(signUpEvent);
    }

    public Long numberOfSignedUpEvents(String email) {
        QSignUpEvent sign = QSignUpEvent.signUpEvent;
        BooleanExpression c = sign.signingUpUser.email.eq(email);
        return repository.count(c);
    }
}
