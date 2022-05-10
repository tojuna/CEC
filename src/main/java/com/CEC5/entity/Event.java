package com.CEC5.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private Boolean isCancelledAndEmailSent;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<Message> signUpMessageList;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<Message> participantForumMessageList;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<User> approvedParticipants;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private Set<User> participantsRequiringApproval;
}