package com.CEC5.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;


@Data
@Entity
public class SignUpEvent {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User signingUpUser;

    @ManyToOne
    private Event event;

    private LocalDateTime signingUpTime;


    public SignUpEvent(User user, Event event, LocalDateTime currentDateTime) {
        this.event = event;
        this.signingUpTime = currentDateTime;
        this.signingUpUser = user;
    }

    public SignUpEvent() {

    }
}
