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
    private Integer unitsConsumed;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private Integer amountDue;

    @Column(nullable = false)
    private Date dueDate;

    @Column(nullable = false)
    private Boolean isPaid=false;

    @Column(nullable = false)
    private Boolean isDiscountApplied=false;

    @Column(nullable = false)
    private Integer discountAmount=0;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @JsonIgnore
    @OneToMany
    private List<Payment> payments;

    @PrePersist
    public void prePersist() {
        if (this.billId == null) {
            this.billId = UUID.randomUUID().toString(); // Auto-generate UUID for billId
        }

        // Automatically set the dueDate to 15 days after endDate
        if (this.endDate != null && this.dueDate == null) {
            LocalDate endLocalDate = this.endDate.toLocalDate();
            this.dueDate = Date.valueOf(endLocalDate.plus(15, ChronoUnit.DAYS));
        }
    }
}
