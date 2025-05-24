package com.expensetrackerapp.application.service.Card;

import com.expensetrackerapp.application.port.in.Card.UpdateCard.UpdateCardRequest;
import com.expensetrackerapp.application.port.in.Card.UpdateCard.UpdateCardUseCase;
import com.expensetrackerapp.application.port.out.Card.GetCardByIdOutboundPort;
import com.expensetrackerapp.application.port.out.Card.UpdateCardOutboundPort;
import com.expensetrackerapp.domain.model.Card;
import com.expensetrackerapp.dto.CardDTO;
import com.expensetrackerapp.infrastructure.outbound.adapters.Card.UpdateCardRepository;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CardMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import com.expensetrackerapp.shared.exceptions.NullRequestException;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class UpdateCardService implements UpdateCardUseCase<CardDTO> {

    private final GetCardByIdOutboundPort<CardEntity> getCardByIdPort;
    private final UpdateCardOutboundPort<CardEntity> updateCardRepository;
    private final CardMapper cardMapper;

    @Override
    public CardDTO updateCard(UpdateCardRequest updateCardRequest, Long cardId) {

        if(Objects.isNull(updateCardRequest)) {
            log.error("Request's body (UpdateCardRequest) cannot be null");
            throw new NullRequestException("Request's body (UpdateCardRequest) cannot be null");
        }

        log.info("Received request to update card with id: {}", cardId);
        Card card;
        try{
            log.info("Updating card with id: {}", cardId);
            CardEntity updatedCard = updateCardRepository.updateCard(cardMapper.fromRequestToPojo(updateCardRequest), cardId);
            updatedCard.setId(cardId);
            return cardMapper.fromEntityToDTO(updatedCard);
        }
        catch (NotFoundInDatabase e){
            throw e;
        }
        catch (IllegalArgumentException | PersistenceException | DataAccessException |
               NullPointerException | ClassCastException e) {
            log.error("Error occurred while updating card: {}", e.getClass().getSimpleName(), e);
            throw new DatabaseInteractionException("Error while updating card: " + e.getClass().getSimpleName());
        }
        catch (MappingException e) {
            throw new MappingException("Error while mapping card: " + e.getClass().getSimpleName());
        }
        catch (Exception e) {
            log.error("Unexpected error occurred while updating card", e);
            throw new DatabaseInteractionException("Unhandled error while updating card.");
        }
    }
}
