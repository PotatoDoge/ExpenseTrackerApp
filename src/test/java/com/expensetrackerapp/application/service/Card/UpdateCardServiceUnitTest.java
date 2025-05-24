package com.expensetrackerapp.application.service.Card;

import com.expensetrackerapp.application.port.in.Card.UpdateCard.UpdateCardRequest;
import com.expensetrackerapp.application.port.out.Card.GetCardByIdOutboundPort;
import com.expensetrackerapp.application.port.out.Card.UpdateCardOutboundPort;
import com.expensetrackerapp.domain.model.Card;
import com.expensetrackerapp.dto.CardDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CardMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import com.expensetrackerapp.shared.exceptions.NullRequestException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCardServiceUnitTest {

    @Mock
    private UpdateCardOutboundPort<CardEntity> updateCardOutboundPort;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private UpdateCardService updateCardService;

    @Test
    void shouldThrowNullRequestExceptionWhenRequestIsNull() {
        Long cardId = 1L;

        NullRequestException exception = assertThrows(NullRequestException.class, () ->
                updateCardService.updateCard(null, cardId)
        );

        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    @Test
    void shouldUpdateCardSuccessfully() {
        Long cardId = 1L;
        UpdateCardRequest request = new UpdateCardRequest();
        Card card = new Card();
        CardEntity updatedEntity = new CardEntity();
        CardDTO expectedDto = new CardDTO();

        when(cardMapper.fromRequestToPojo(request)).thenReturn(card);
        when(updateCardOutboundPort.updateCard(card, cardId)).thenReturn(updatedEntity);
        when(cardMapper.fromEntityToDTO(updatedEntity)).thenReturn(expectedDto);

        CardDTO result = updateCardService.updateCard(request, cardId);

        assertNotNull(result);
        assertEquals(expectedDto, result);

        verify(cardMapper).fromRequestToPojo(request);
        verify(updateCardOutboundPort).updateCard(card, cardId);
        verify(cardMapper).fromEntityToDTO(updatedEntity);
    }

    @Test
    void shouldThrowNotFoundInDatabaseIfUpdateFailsDueToNotFound() {
        Long cardId = 999L;
        UpdateCardRequest request = new UpdateCardRequest();
        Card card = new Card();

        when(cardMapper.fromRequestToPojo(request)).thenReturn(card);
        when(updateCardOutboundPort.updateCard(card, cardId)).thenThrow(new NotFoundInDatabase("Card not found"));

        assertThrows(NotFoundInDatabase.class, () ->
                updateCardService.updateCard(request, cardId)
        );
    }

    @Test
    void shouldThrowMappingExceptionWhenMappingFails() {
        Long cardId = 1L;
        UpdateCardRequest request = new UpdateCardRequest();

        when(cardMapper.fromRequestToPojo(request)).thenThrow(new MappingException("Mapping failed"));

        assertThrows(MappingException.class, () ->
                updateCardService.updateCard(request, cardId)
        );
    }

    @Test
    void shouldThrowDatabaseInteractionExceptionOnPersistenceException() {
        Long cardId = 1L;
        UpdateCardRequest request = new UpdateCardRequest();
        Card card = new Card();

        when(cardMapper.fromRequestToPojo(request)).thenReturn(card);
        when(updateCardOutboundPort.updateCard(card, cardId)).thenThrow(new PersistenceException());

        assertThrows(DatabaseInteractionException.class, () ->
                updateCardService.updateCard(request, cardId)
        );
    }

    @Test
    void shouldThrowDatabaseInteractionExceptionOnDataAccessException() {
        Long cardId = 1L;
        UpdateCardRequest request = new UpdateCardRequest();
        Card card = new Card();
        DataAccessException exception = mock(DataAccessException.class);

        when(cardMapper.fromRequestToPojo(request)).thenReturn(card);
        when(updateCardOutboundPort.updateCard(card, cardId)).thenThrow(exception);

        assertThrows(DatabaseInteractionException.class, () ->
                updateCardService.updateCard(request, cardId)
        );
    }

    @Test
    void shouldThrowDatabaseInteractionExceptionOnUnhandledException() {
        Long cardId = 1L;
        UpdateCardRequest request = new UpdateCardRequest();
        Card card = new Card();

        when(cardMapper.fromRequestToPojo(request)).thenReturn(card);
        when(updateCardOutboundPort.updateCard(card, cardId)).thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(DatabaseInteractionException.class, () ->
                updateCardService.updateCard(request, cardId)
        );
    }
}
