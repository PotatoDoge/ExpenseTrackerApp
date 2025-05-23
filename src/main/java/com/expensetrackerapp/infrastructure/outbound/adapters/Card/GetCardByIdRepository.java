package com.expensetrackerapp.infrastructure.outbound.adapters.Card;

import com.expensetrackerapp.application.port.out.Card.GetCardByIdOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.repositories.CardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
@Log4j2
public class GetCardByIdRepository implements GetCardByIdOutboundPort<CardEntity> {

    private final CardRepository cardRepository;

    @Override
    public Optional<CardEntity> getCardById(Long cardId) {
        return cardRepository.findById(cardId);
    }
}
