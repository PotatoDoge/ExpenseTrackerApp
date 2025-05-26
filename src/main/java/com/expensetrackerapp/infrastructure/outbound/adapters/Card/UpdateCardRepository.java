package com.expensetrackerapp.infrastructure.outbound.adapters.Card;

import com.expensetrackerapp.application.port.out.Card.UpdateCardOutboundPort;
import com.expensetrackerapp.domain.model.Card;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CardMapper;
import com.expensetrackerapp.infrastructure.outbound.repositories.CardRepository;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
@Log4j2
public class UpdateCardRepository implements UpdateCardOutboundPort<CardEntity> {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Override
    public CardEntity updateCard(Card cardToBeUpdated, Long cardId) {

        Optional<CardEntity> cardToBeUpdatedEntity = cardRepository.findById(cardId);
        if(cardToBeUpdatedEntity.isEmpty()) {
            log.warn("Card with id {} not found", cardId);
            throw new NotFoundInDatabase("Card with id " + cardId + " not found in database");
        }

        CardEntity existingCard = cardToBeUpdatedEntity.get();
        cardMapper.updateEntity(existingCard, cardMapper.fromPojoToEntity(cardToBeUpdated));
        cardRepository.save(existingCard);
        log.info("Updated card: {}", existingCard);
        return existingCard;
    }
}
