package com.CEC5.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ReviewsAsParticipants {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reviewedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reviewedBy;
    private Integer rating;
    private String description;
}
