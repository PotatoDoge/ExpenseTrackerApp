package com.expensetrackerapp.application.port.in.Expense.SaveExpense;

public interface SaveExpenseUseCase <T> {
    T saveExpense(SaveExpenseRequest request);
}
