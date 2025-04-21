package com.expensetrackerapp.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Data
public class ExpenseDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal amount;
    private Currency currency;
    private LocalDateTime dateTime;
    private boolean requiresInvoice;
    private boolean isPaidInFull;
    private int installments;
    private boolean isRecurring;
    private String vendor;
    private String location;

    // TODO: Add missing Expense fields; these are msising due to missing POJOs dtos

}
