package com.CEC5.entity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "USER")
public class User {
    @Id
    @NotEmpty
    private String email;

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

    @NotNull
    private Address address;

    @OneToMany(mappedBy = "organizer")
    private List<Event> createdEvents = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    private List<Reviews> reviewsReceivedAsOrganizerList;

    private Float reviewsReceivedAsOrganizerAverage = 0F;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Reviews> reviewsReceivedAsParticipantList;

    private Float reviewsReceivedAsParticipantAverage = 0F;
}
