package com.expensetrackerapp.application.port.in.Card.SaveCard;

public interface SaveCardUseCase <T> {
    T saveCard(SaveCardRequest request);
}
