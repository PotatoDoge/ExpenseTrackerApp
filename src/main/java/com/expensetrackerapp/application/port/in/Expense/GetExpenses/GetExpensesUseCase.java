package com.expensetrackerapp.application.port.in.Expense.GetExpenses;

import java.util.List;

public interface GetExpensesUseCase<T,F>{
    List<T> getExpenses(F filter);
}
