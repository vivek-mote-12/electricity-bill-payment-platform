package com.finzly.bharat_bijili_co.bill_payment_platform.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String walletId;

    @OneToOne
    @JoinColumn(nullable = false)
    private Customer customer;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @PrePersist
    protected void prePersist() {
        this.walletId = UUID.randomUUID().toString();
    }
}
