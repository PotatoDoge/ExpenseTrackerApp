package com.expensetrackerapp.application.port.in.SaveExpense;

import com.expensetrackerapp.domain.enums.PaymentMethod;
import com.expensetrackerapp.domain.enums.RecurrenceType;
import com.expensetrackerapp.domain.model.Attachment;
import com.expensetrackerapp.domain.model.Card;
import com.expensetrackerapp.domain.model.Category;
import com.expensetrackerapp.domain.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveExpenseRequest {
    private String name;
    private String description;
    private BigDecimal amount;
    private Currency currency;
    private LocalDateTime dateTime;
    private PaymentMethod paymentMethod;
    private boolean requiresInvoice;
    private boolean isPaidInFull;
    private int installments;
    private boolean isRecurring;
    private RecurrenceType recurrenceType;
    private String vendor;
    private String location;
    private Card card;
    private Category category;
    private Set<Tag> tags;
    private Set<Attachment> attachments;
}