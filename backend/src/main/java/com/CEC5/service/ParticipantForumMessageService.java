package com.CEC5.service;

import com.CEC5.entity.ParticipantForumMessage;
import com.CEC5.repository.ParticipantForumMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantForumMessageService {

    @Autowired
    ParticipantForumMessageRepository participantForumMessageRepository;

    public ParticipantForumMessage save(ParticipantForumMessage participantForumMessage) {
        return participantForumMessageRepository.save(participantForumMessage);
    }
}
