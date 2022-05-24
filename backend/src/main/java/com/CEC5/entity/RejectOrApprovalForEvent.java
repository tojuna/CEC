package com.CEC5.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Entity
public class RejectOrApprovalForEvent {

    @Id
    @GeneratedValue
    private Long id;

    private Boolean isReject;

    private LocalDateTime timeOfRequest;

    @ManyToOne
    private User userOnWhoActionIsApplied;

    @ManyToOne
    private Event event;

    public RejectOrApprovalForEvent(boolean b, LocalDateTime currentDateTime, User toBeApproved, Event event) {
        this.isReject = b;
        this.timeOfRequest = currentDateTime;
        this.userOnWhoActionIsApplied = toBeApproved;
        this.event = event;
    }

    public RejectOrApprovalForEvent() {

    }
}
