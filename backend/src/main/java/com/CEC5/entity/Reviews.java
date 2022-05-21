package com.CEC5.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Reviews {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties({"email", "screenName"})
    private User reviewedUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties({"email", "screenName"})
    private User reviewedBy;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties("event_id")
    private Event event;

    @NotNull
    private Float rating;

    private String description;
}
