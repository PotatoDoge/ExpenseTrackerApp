package com.expensetrackerapp.application.service.Card;

import com.expensetrackerapp.application.port.out.Card.GetCardByIdOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.adapters.Card.DeleteCardRepository;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import jakarta.persistence.PersistenceException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCardServiceUnitTest {

    @Mock
    private DeleteCardRepository deleteCardRepository;

    @Mock
    private GetCardByIdOutboundPort<CardEntity> getCardByIdPort;

    @InjectMocks
    private DeleteCardService deleteCardService;

    @Test
    void shouldDeleteCardSuccessfully() {
        Long cardId = 1L;
        CardEntity cardEntity = new CardEntity();
        when(getCardByIdPort.getCardById(cardId)).thenReturn(Optional.of(cardEntity));

        deleteCardService.deleteCard(cardId);

        verify(getCardByIdPort).getCardById(cardId);
        verify(deleteCardRepository, times(1)).deleteCard(cardId); // It's called twice in your code
    }

    @Test
    void shouldThrowNotFoundInDatabaseWhenCardDoesNotExist() {
        Long cardId = 2L;
        when(getCardByIdPort.getCardById(cardId)).thenReturn(Optional.empty());

        NotFoundInDatabase exception = assertThrows(NotFoundInDatabase.class, () ->
                deleteCardService.deleteCard(cardId)
        );

        assertEquals("Card with id: " + cardId + " does not exist", exception.getMessage());
        verify(deleteCardRepository, never()).deleteCard(any());
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        Long cardId = 3L;
        when(getCardByIdPort.getCardById(cardId)).thenReturn(Optional.of(new CardEntity()));
        doThrow(new IllegalArgumentException("Invalid argument")).when(deleteCardRepository).deleteCard(cardId);

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () ->
                deleteCardService.deleteCard(cardId)
        );

        assertTrue(exception.getMessage().contains("Error occurred while deleting card with id"));
    }

    @Test
    void shouldHandlePersistenceException() {
        Long cardId = 4L;
        when(getCardByIdPort.getCardById(cardId)).thenReturn(Optional.of(new CardEntity()));
        doThrow(new PersistenceException("DB error")).when(deleteCardRepository).deleteCard(cardId);

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () ->
                deleteCardService.deleteCard(cardId)
        );

        assertTrue(exception.getMessage().contains("Error occurred while deleting card"));
    }

    @Test
    void shouldHandleDataAccessException() {
        Long cardId = 5L;
        when(getCardByIdPort.getCardById(cardId)).thenReturn(Optional.of(new CardEntity()));
        doThrow(mock(DataAccessException.class)).when(deleteCardRepository).deleteCard(cardId);

        assertThrows(DatabaseInteractionException.class, () ->
                deleteCardService.deleteCard(cardId)
        );
    }

    @Test
    void shouldHandleUnexpectedException() {
        Long cardId = 6L;
        when(getCardByIdPort.getCardById(cardId)).thenReturn(Optional.of(new CardEntity()));
        doThrow(new RuntimeException("Unexpected")).when(deleteCardRepository).deleteCard(cardId);

        assertThrows(DatabaseInteractionException.class, () ->
                deleteCardService.deleteCard(cardId)
        );
    }
}
