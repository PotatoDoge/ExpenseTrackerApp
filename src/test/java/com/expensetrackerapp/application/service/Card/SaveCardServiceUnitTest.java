package com.expensetrackerapp.application.service.Card;

import com.expensetrackerapp.application.port.in.Card.SaveCard.SaveCardRequest;
import com.expensetrackerapp.application.port.out.Card.SaveCardOutboundPort;
import com.expensetrackerapp.domain.model.Card;
import com.expensetrackerapp.dto.CardDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CardMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
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
class SaveCardServiceUnitTest {

    @Mock
    private SaveCardOutboundPort<CardEntity> saveCardOutboundPort;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private SaveCardService saveCardService;

    @Test
    void shouldThrowNullRequestExceptionWhenRequestIsNull() {
        NullRequestException exception = assertThrows(NullRequestException.class, () ->
                saveCardService.saveCard(null)
        );

        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    @Test
    void shouldSaveCardSuccessfully() {
        SaveCardRequest request = new SaveCardRequest();
        Card card = new Card();
        CardEntity savedEntity = new CardEntity();
        CardDTO expectedDto = new CardDTO();

        when(cardMapper.fromRequestToPojo(request)).thenReturn(card);
        when(saveCardOutboundPort.saveCard(card)).thenReturn(savedEntity);
        when(cardMapper.fromEntityToDTO(savedEntity)).thenReturn(expectedDto);

        CardDTO result = saveCardService.saveCard(request);

        assertNotNull(result);
        assertEquals(expectedDto, result);

        verify(cardMapper).fromRequestToPojo(request);
        verify(saveCardOutboundPort).saveCard(card);
        verify(cardMapper).fromEntityToDTO(savedEntity);
    }

    @Test
    void shouldThrowMappingExceptionWhenMappingFails() {
        SaveCardRequest request = new SaveCardRequest();

        when(cardMapper.fromRequestToPojo(request)).thenThrow(new MappingException("Mapping failed"));

        assertThrows(MappingException.class, () ->
                saveCardService.saveCard(request)
        );
    }

    @Test
    void shouldThrowDatabaseInteractionExceptionOnPersistenceException() {
        SaveCardRequest request = new SaveCardRequest();
        Card card = new Card();

        when(cardMapper.fromRequestToPojo(request)).thenReturn(card);
        when(saveCardOutboundPort.saveCard(card)).thenThrow(new PersistenceException("DB error"));

        assertThrows(DatabaseInteractionException.class, () ->
                saveCardService.saveCard(request)
        );
    }

    @Test
    void shouldThrowDatabaseInteractionExceptionOnDataAccessException() {
        SaveCardRequest request = new SaveCardRequest();
        Card card = new Card();
        DataAccessException dataAccessException = mock(DataAccessException.class);

        when(cardMapper.fromRequestToPojo(request)).thenReturn(card);
        when(saveCardOutboundPort.saveCard(card)).thenThrow(dataAccessException);

        assertThrows(DatabaseInteractionException.class, () ->
                saveCardService.saveCard(request)
        );
    }

    @Test
    void shouldThrowDatabaseInteractionExceptionOnUnknownException() {
        SaveCardRequest request = new SaveCardRequest();
        Card card = new Card();

        when(cardMapper.fromRequestToPojo(request)).thenReturn(card);
        when(saveCardOutboundPort.saveCard(card)).thenThrow(new RuntimeException("Unexpected"));

        assertThrows(DatabaseInteractionException.class, () ->
                saveCardService.saveCard(request)
        );
    }
}
