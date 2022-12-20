package com.springstatemachine.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class OrderInvoice {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDate localDate;
    private String state;

    @Transient
    String event;

    @Transient
    String paymentType;
}
