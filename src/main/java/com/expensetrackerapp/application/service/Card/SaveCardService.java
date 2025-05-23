package com.expensetrackerapp.application.service.Card;

import com.expensetrackerapp.application.port.in.Card.SaveCard.SaveCardRequest;
import com.expensetrackerapp.application.port.in.Card.SaveCard.SaveCardUseCase;
import com.expensetrackerapp.application.port.out.Card.SaveCardOutboundPort;
import com.expensetrackerapp.domain.model.Card;
import com.expensetrackerapp.dto.CardDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CardMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import com.expensetrackerapp.shared.exceptions.NullRequestException;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
@Log4j2
public class SaveCardService implements SaveCardUseCase<CardDTO> {

    private final SaveCardOutboundPort<CardEntity> saveCardOutboundPortPort;
    private final CardMapper cardMapper;

    @Override
    public CardDTO saveCard(SaveCardRequest saveCardRequest) {

        if (Objects.isNull(saveCardRequest)) {
            log.error("Request's body (SaveCardRequest) cannot be null");
            throw new NullRequestException("Request's body (SaveCardRequest) cannot be null");
        }

        Card card;

        try{
            card = cardMapper.fromRequestToPojo(saveCardRequest);
            log.info("Saving card: {}", card);
            CardEntity cardEntity = saveCardOutboundPortPort.saveCard(card);
            return cardMapper.fromEntityToDTO(cardEntity);
        }
        catch (IllegalArgumentException | PersistenceException | DataAccessException |
               NullPointerException | ClassCastException e) {
            log.error("Error occurred while saving card: {}", e.getClass().getSimpleName(), e);
            throw new DatabaseInteractionException("Error while saving card: " + e.getClass().getSimpleName());
        }
        catch (MappingException e) {
            throw new MappingException("Error while mapping card: " + e.getClass().getSimpleName());
        }
        catch (Exception e) {
            log.error("Unexpected error occurred while saving card", e);
            throw new DatabaseInteractionException("Unhandled error while saving card.");
        }
    }
}
