package com.CEC5.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    private String email;
    private Boolean organization;
    private String fullName;
    private String screenName;
    private String gender;
    private String description;
    private Address address;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<Event> createdEvents;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<Message> signUpMessageList;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<Message> participantForumMessageList;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ReviewsAsOrganizer> reviewsReceivedAsOrganizerList;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ReviewsAsParticipants> reviewsReceivedAsParticipantList;

}
