package com.finzly.bharat_bijili_co.bill_payment_platform.model;

import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentMethod;
import com.finzly.bharat_bijili_co.bill_payment_platform.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,unique = true)
    private String paymentId;

    @ManyToOne
    @JoinColumn
    private Bill bill;

    @ManyToOne
    @JoinColumn
    private Customer customer;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Date paymentDate;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private String txnRefId;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.paymentId == null) {
            this.paymentId = UUID.randomUUID().toString(); // Auto-generate UUID for paymentId
        }
    }
}
