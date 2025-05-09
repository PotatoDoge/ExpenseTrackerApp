package com.expensetrackerapp.application.port.in.Expense.UpdateExpense;


public interface UpdateExpenseUseCase <T> {
    T updateExpense(UpdateExpenseRequest request, Long expenseId);
}
