package com.expensetrackerapp.application.port.in.Card.UpdateCard;

import com.expensetrackerapp.application.port.in.Expense.UpdateExpense.UpdateExpenseRequest;

public interface UpdateCardUseCase<T> {
    T updateCard(UpdateCardRequest updateCardRequest, Long cardId);
}
