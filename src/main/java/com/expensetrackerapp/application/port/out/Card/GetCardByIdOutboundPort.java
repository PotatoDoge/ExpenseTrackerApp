package com.expensetrackerapp.application.port.out.Card;

import java.util.Optional;

public interface GetCardByIdOutboundPort <T> {
    Optional<T> getCardById(Long cardId);
}
