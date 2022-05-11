package com.CEC5.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Event {
    @Id
    @GeneratedValue
    private Long event_id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties("email")
    private User organizer;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    @NotNull
    private LocalDateTime signUpDeadline;

    @NotNull
    private Address address;

    @NotNull
    private Integer minParticipants;

    @NotNull
    private Integer maxParticipants;

    @NotNull
    private Integer fee;

    @NotNull
    private Boolean isFirstComeFirstServe;

    @Column(columnDefinition = "boolean default false")
    private Boolean isCancelledAndEmailSent;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Message> signUpMessageList;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Message> participantForumMessageList;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<User> approvedParticipants;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<User> participantsRequiringApproval;
}