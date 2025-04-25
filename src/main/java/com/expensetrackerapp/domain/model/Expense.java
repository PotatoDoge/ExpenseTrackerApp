package com.expensetrackerapp.domain.model;

import com.expensetrackerapp.domain.enums.PaymentMethod;
import com.expensetrackerapp.domain.enums.RecurrenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    private Long id;
    private String name;
    private String description;
    private BigDecimal amount;
    private String currency;
    private LocalDate expenseDate;
    private PaymentMethod paymentMethod;
    private Boolean requiresInvoice;
    private Boolean isPaidInFull;
    private Integer installments;
    private Boolean isRecurring;
    private RecurrenceType recurrenceType;
    private String vendor;
    private String location;

    private Card card;
    private Category category;
    private Set<Tag> tags;
    private Set<Attachment> attachments;
}