package com.CEC5.entity;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Address {
    private String street;
    private String number;
    private String city;
    private String state;
    private String zip;
}
