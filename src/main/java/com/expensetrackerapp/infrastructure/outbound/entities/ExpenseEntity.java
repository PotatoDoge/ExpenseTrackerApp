package com.expensetrackerapp.infrastructure.outbound.entities;

import com.expensetrackerapp.domain.enums.PaymentMethod;
import com.expensetrackerapp.domain.enums.RecurrenceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal amount;
    private String currency;
    @Column(columnDefinition = "date")
    private LocalDate expenseDate;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private Boolean requiresInvoice;
    private Boolean isPaidInFull;
    private Integer installments;
    private Boolean isRecurring;
    @Enumerated(EnumType.STRING)
    private RecurrenceType recurrenceType;
    private String vendor;
    private String location;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    // TODO: Relationships for card, tags, attachments, etc
}
