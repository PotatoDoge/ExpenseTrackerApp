package com.expensetrackerapp.application.port.out.Expense;

import java.util.List;

public interface GetExpensesOutboundPort<T,F> {
    List<T> getExpenses(F filters);
}