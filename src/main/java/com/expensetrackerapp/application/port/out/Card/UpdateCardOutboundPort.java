package com.expensetrackerapp.application.port.out.Card;

import com.expensetrackerapp.domain.model.Card;

public interface UpdateCardOutboundPort<T> {
    T updateCard(Card card, Long cardId);
}
