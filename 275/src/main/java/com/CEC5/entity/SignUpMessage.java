package com.CEC5.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class SignUpMessage {
    @Id
    @GeneratedValue
    private Long messageId;
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    private User messageCreator;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
    private String imageUrl;
}
