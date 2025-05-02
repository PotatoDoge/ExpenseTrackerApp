package com.expensetrackerapp.application.port.in.UpdateExpense;


public interface UpdateExpenseUseCase <T> {
    T updateExpense(UpdateExpenseRequest request, Long expenseId);
}
