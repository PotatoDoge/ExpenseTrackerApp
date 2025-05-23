package com.expensetrackerapp.application.service.Card;

import com.expensetrackerapp.application.port.in.Card.GetCards.GetCardsFilter;
import com.expensetrackerapp.application.port.in.Card.GetCards.GetCardsUseCase;
import com.expensetrackerapp.application.port.out.Card.GetCardsOutboundPort;
import com.expensetrackerapp.dto.CardDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CardMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class GetCardsService implements GetCardsUseCase <CardDTO, GetCardsFilter> {

    private final GetCardsOutboundPort<CardEntity, GetCardsFilter> getCardsPort;
    private final CardMapper cardMapper;

    @Override
    public List<CardDTO> getCards(GetCardsFilter filter) {
        log.info("Received request to get cards with filter: {}", filter);
        try{
            return getCardsPort.getCards(filter).stream().map(cardMapper::fromEntityToDTO).toList();
        }
        catch (MappingException e) {
            throw new MappingException("Error while mapping card from entity to DTO: " + e.getClass().getSimpleName());
        }
        catch (IllegalArgumentException | PersistenceException | DataAccessException |
               NullPointerException | ClassCastException e) {
            log.error("Error occurred while fetching cards: {} - {}", e.getClass().getSimpleName(), e.getMessage(), e);
            throw new DatabaseInteractionException("Error while fetching cards: " + e.getClass().getSimpleName());
        }
        catch (Exception e) {
            log.error("Error occurred while fetching cards: {} - {}", e.getClass().getSimpleName(), e.getMessage(), e);
            throw new DatabaseInteractionException("Unhandled error while fetching all cards.");
        }
    }
}