package com.expensetrackerapp.infrastructure.outbound.adapters.Card;

import com.expensetrackerapp.application.port.in.Card.GetCards.GetCardsFilter;
import com.expensetrackerapp.application.port.out.Card.GetCardsOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.repositories.CardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Log4j2
public class GetCardsRepository implements GetCardsOutboundPort <CardEntity, GetCardsFilter> {

    private final CardRepository cardRepository;

    @Override
    public List<CardEntity> getCards(GetCardsFilter filters) {
        List<CardEntity> cardEntities = cardRepository.findAllCardsByFilters(
                filters.getId(),
                filters.getName(),
                filters.getType(),
                filters.getLastDigits(),
                filters.getBankName()
        );
        log.info("Fetched cards: {}", cardEntities.size());
        return cardEntities;
    }
}
