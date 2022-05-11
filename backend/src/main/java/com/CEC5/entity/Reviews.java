package com.CEC5.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Reviews {
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
