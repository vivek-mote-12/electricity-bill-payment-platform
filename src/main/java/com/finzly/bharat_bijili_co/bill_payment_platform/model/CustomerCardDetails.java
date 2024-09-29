package com.finzly.bharat_bijili_co.bill_payment_platform.model;

//import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.util.UUID;
//import java.time.LocalDateTime;
//import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table (name = "CustomerCardDetails")
public class CustomerCardDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cardId", nullable = false)
    private String cardId;

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


    @PrePersist
    public void prePersist() {
        if (this.cardId == null) {
            this.cardId = UUID.randomUUID().toString(); // Auto-generate UUID for cardId
        }
    }
}