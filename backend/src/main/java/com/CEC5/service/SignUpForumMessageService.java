package com.CEC5.service;

import com.CEC5.entity.SignUpForumMessage;
import com.CEC5.repository.SignUpForumMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpForumMessageService {

    @Autowired
    SignUpForumMessageRepository signUpForumMessageRepository;

    public SignUpForumMessage save(SignUpForumMessage signUpForumMessage) {
        return signUpForumMessageRepository.save(signUpForumMessage);
    }

}
