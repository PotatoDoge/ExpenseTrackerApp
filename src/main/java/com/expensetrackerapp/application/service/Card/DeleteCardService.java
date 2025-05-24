package com.expensetrackerapp.application.service.Card;

import com.expensetrackerapp.application.port.in.Card.DeleteCard.DeleteCardUseCase;
import com.expensetrackerapp.application.port.out.Card.GetCardByIdOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.adapters.Card.DeleteCardRepository;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class DeleteCardService implements DeleteCardUseCase {

    private final DeleteCardRepository deleteCardRepository;
    private final GetCardByIdOutboundPort<CardEntity> getCardByIdPort;

    @Override
    public void deleteCard(Long cardId) {
        log.info("Received request to delete card with id: {}", cardId);

        try {
            Optional<CardEntity> existingCard = getCardByIdPort.getCardById(cardId);
            if (existingCard.isEmpty()) {
                log.warn("Card with id: {} does not exist", cardId);
                throw new NotFoundInDatabase("Card with id: " + cardId + " does not exist");
            }
            log.info("Deleting card with id: {}", cardId);
            deleteCardRepository.deleteCard(cardId);
        }
        catch (NotFoundInDatabase e) {
            throw e;
        }
        catch (IllegalArgumentException | PersistenceException | DataAccessException |
               NullPointerException | ClassCastException e) {
            log.error("Error occurred while deleting card with id: {}", cardId, e);
            throw new DatabaseInteractionException("Error occurred while deleting card with id: " + cardId);
        }
        catch (Exception e) {
            log.error("Unexpected error occurred while deleting card with id: {}", cardId, e);
            throw new DatabaseInteractionException("Unexpected error occurred while deleting card with id: " + cardId);
        }
    }
}
