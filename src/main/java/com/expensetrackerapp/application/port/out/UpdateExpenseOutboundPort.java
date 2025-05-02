package com.expensetrackerapp.application.port.out;

import com.expensetrackerapp.domain.model.Expense;

public interface UpdateExpenseOutboundPort<T> {

    T updateExpense(Expense expense, Long expenseId);

}
