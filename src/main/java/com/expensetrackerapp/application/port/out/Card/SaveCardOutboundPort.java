package com.expensetrackerapp.application.port.out.Card;

import com.expensetrackerapp.domain.model.Card;
import com.expensetrackerapp.domain.model.Expense;

public interface SaveCardOutboundPort<T> {
    T saveCard(Card card);
}