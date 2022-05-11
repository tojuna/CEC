package com.CEC5.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    @NotEmpty
    private String email;

    @JsonIgnore
    @NotNull
    private String password;

    @NotNull
    private Boolean organization;

    @NotEmpty
    private String fullName;

    @NotEmpty
    private String screenName;

    @NotEmpty
    private String gender;

    @NotEmpty
    private String description;

//    @NotEmpty
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
    private List<Reviews> reviewsReceivedAsOrganizerList;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Reviews> reviewsReceivedAsParticipantList;
}
