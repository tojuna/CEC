package com.CEC5.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Reviews {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties({"email", "screenName"})
    private User reviewedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties({"email", "screenName"})
    private User reviewedBy;

    private Float rating;

    private String description;
}
