package com.expensetrackerapp.application.port.in.SaveExpense;

public interface SaveExpenseUseCase <T> {
    T saveExpense(SaveExpenseRequest request);
}
