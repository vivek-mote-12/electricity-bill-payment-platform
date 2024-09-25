package com.finzly.bharat_bijili_co.bill_payment_platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,unique = true)
    private String billId;

    @JoinColumn
    @ManyToOne
    private Customer customer;

    @Column(nullable = false)
    private Long unitsConsumed;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private Double amountDue;

    @Column(nullable = false)
    private Date dueDate;

    @Column(nullable = false)
    private Boolean isPaid;

    @Column()
    private String paymentId;

    @Column(nullable = false)
    private Boolean isDiscountApplied;

    @Column(nullable = false)
    private Integer discountAmount;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @JsonIgnore
    @OneToMany
    private List<Payment> payments;

    @PrePersist
    public void prePersist() {
        this.discountAmount=0;
        this.isDiscountApplied=false;
        this.isPaid=false;
        this.paymentId=null;

        if (this.billId == null) {
            this.billId = UUID.randomUUID().toString(); // Auto-generate UUID for billId
        }
    }
}
