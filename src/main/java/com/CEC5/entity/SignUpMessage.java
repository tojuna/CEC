package com.CEC5.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.LAZY)
    List<SignUpMessage> replies;
}
