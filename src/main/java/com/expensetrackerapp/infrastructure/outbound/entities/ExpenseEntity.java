package com.expensetrackerapp.infrastructure.outbound.entities;

import com.expensetrackerapp.domain.enums.PaymentMethod;
import com.expensetrackerapp.domain.enums.RecurrenceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

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
    private Currency currency;
    private LocalDateTime dateTime;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private boolean requiresInvoice;
    private boolean isPaidInFull;
    private int installments;
    private boolean isRecurring;
    @Enumerated(EnumType.STRING)
    private RecurrenceType recurrenceType;
    private String vendor;
    private String location;

    // TODO: Relationships for card, category, tags, attachments, etc
}
