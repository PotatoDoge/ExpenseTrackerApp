package com.expensetrackerapp.application.port.out.Card;

import java.util.List;

public interface GetCardsOutboundPort <T,F> {
    List<T> getCards(F filters);
}