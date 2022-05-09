package com.CEC5.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Event {
    @Id
    @GeneratedValue
    private Long eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User organizer;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime signUpDeadline;
    private Address address;
    private Integer minParticipants;
    private Integer maxParticipants;
    private Integer fee;
    private Boolean isFirstComeFirstServe;
    private String status;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<SignUpMessage> signUpMessageList;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<ParticipantForumMessage> participantForumMessageList;
}