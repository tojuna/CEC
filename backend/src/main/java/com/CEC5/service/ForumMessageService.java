package com.CEC5.service;

import com.CEC5.entity.ForumMessage;
import com.CEC5.repository.ForumMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForumMessageService {

    @Autowired
    ForumMessageRepository forumMessageRepository;

    public ForumMessage save(ForumMessage forumMessage) {
        return forumMessageRepository.save(forumMessage);
    }
}
