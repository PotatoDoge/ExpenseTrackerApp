package com.expensetrackerapp.application.port.base;

import com.expensetrackerapp.domain.enums.PaymentMethod;
import com.expensetrackerapp.domain.enums.RecurrenceType;
import com.expensetrackerapp.domain.model.Attachment;
import com.expensetrackerapp.domain.model.Card;
import com.expensetrackerapp.domain.model.Category;
import com.expensetrackerapp.domain.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class BaseExpenseRequest {

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
