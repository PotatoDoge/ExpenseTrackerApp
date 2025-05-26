package com.expensetrackerapp.infrastructure.outbound.adapters.Card;

import com.expensetrackerapp.application.port.out.Card.SaveCardOutboundPort;
import com.expensetrackerapp.domain.model.Card;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CardMapper;
import com.expensetrackerapp.infrastructure.outbound.repositories.CardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class SaveCardRepository implements SaveCardOutboundPort<CardEntity> {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Override
    public CardEntity saveCard(Card expense) {
        CardEntity savedCard = cardRepository.save(cardMapper.fromPojoToEntity(expense));
        log.info("Saved card: {}", savedCard);
        return savedCard;
    }
}
