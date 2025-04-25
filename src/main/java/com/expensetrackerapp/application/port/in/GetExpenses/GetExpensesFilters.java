package com.expensetrackerapp.application.port.in.GetExpenses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class GetExpensesFilters {
    private Long expenseId;
    private String expenseName;
    private String description;
    private BigDecimal amount;
    private String currency;
    private LocalDate expenseDate;
    private Boolean requiresInvoice;
    private Integer installments;
    private String vendor;
    private String location;
    private Boolean paidInFull;
    private Boolean recurring;
}
