package com.expensetrackerapp.application.port.out.Expense;

import com.expensetrackerapp.domain.model.Expense;

public interface SaveExpenseOutboundPort<T> {
    T saveExpense(Expense expense);
}