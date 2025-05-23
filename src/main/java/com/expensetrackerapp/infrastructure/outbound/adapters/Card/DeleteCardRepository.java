package com.expensetrackerapp.infrastructure.outbound.adapters.Card;

import com.expensetrackerapp.application.port.out.Card.DeleteCardOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.repositories.CardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class DeleteCardRepository implements DeleteCardOutboundPort {

    private final CardRepository cardRepository;

    @Override
    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }
}
