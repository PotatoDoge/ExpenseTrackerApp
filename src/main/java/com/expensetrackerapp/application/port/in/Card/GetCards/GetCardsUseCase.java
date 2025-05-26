package com.expensetrackerapp.application.port.in.Card.GetCards;

import java.util.List;

public interface GetCardsUseCase <T,F>{
    List<T> getCards(F filter);
}
