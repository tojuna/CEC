package com.CEC5.entity;


import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class ForumMessage {
    @Id
    @GeneratedValue
    private Long messageId;

    @NotNull
    private String message;

    @NotNull
    @JsonIncludeProperties({"email", "screenName"})
    @ManyToOne(fetch = FetchType.LAZY)
    private User messageCreator;

    @NotNull
    @JsonIncludeProperties({"event_id"})
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    private String imageUrl;
}
