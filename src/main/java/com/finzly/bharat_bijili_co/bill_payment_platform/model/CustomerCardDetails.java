package com.finzly.bharat_bijili_co.bill_payment_platform.model;

//import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
//import java.time.LocalDateTime;
//import java.util.List;

@Entity
@Table (name = "CustomerCardDetails")
public class CustomerCardDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cardId", nullable = false, unique = true)
    private Integer cardId;

    @JoinColumn
    @ManyToOne
    private Customer customer;

    @Column
    private String cardType;

    @Column
    private String cardNetwork;

    @Column
    private String cardLastFour;

    @Column
    private String cardToken;

    @Column
    private String cardHolderName;

    @Column
    private String expiryMonth;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}