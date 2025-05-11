package com.expensetrackerapp.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ExpenseDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal amount;
    private String currency;
    private LocalDate expenseDate;
    private Boolean requiresInvoice;
    private Boolean isPaidInFull;
    private Integer installments;
    private Boolean isRecurring;
    private String vendor;
    private String location;
    private CategoryDTO category;

    // TODO: Add missing Expense fields; these are msising due to missing POJOs dtos

}
