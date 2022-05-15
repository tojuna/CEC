package com.CEC5.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
public class SignUpForumMessage {
    @Id
    @GeneratedValue
    private Long messageId;

    @NotNull
    private String message;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties({"email", "screenName"})
    private User messageCreator;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties({"event_id"})
    private Event event;

    private String imageUrl;
}
