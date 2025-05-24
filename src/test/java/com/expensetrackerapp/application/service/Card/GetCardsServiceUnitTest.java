package com.expensetrackerapp.application.service.Card;

import com.expensetrackerapp.application.port.in.Card.GetCards.GetCardsFilter;
import com.expensetrackerapp.application.port.out.Card.GetCardsOutboundPort;
import com.expensetrackerapp.dto.CardDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CardMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCardsServiceUnitTest {

    @Mock
    private GetCardsOutboundPort<CardEntity, GetCardsFilter> getCardsPort;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private GetCardsService getCardsService;

    @Test
    void shouldReturnListOfCardDTOs() {
        GetCardsFilter filter = new GetCardsFilter();
        CardEntity cardEntity = new CardEntity();
        CardDTO cardDTO = new CardDTO();

        when(getCardsPort.getCards(filter)).thenReturn(List.of(cardEntity));
        when(cardMapper.fromEntityToDTO(cardEntity)).thenReturn(cardDTO);

        List<CardDTO> result = getCardsService.getCards(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cardDTO, result.getFirst());

        verify(getCardsPort).getCards(filter);
        verify(cardMapper).fromEntityToDTO(cardEntity);
    }

    @Test
    void shouldReturnEmptyListWhenNoCardsFound() {
        GetCardsFilter filter = new GetCardsFilter();

        when(getCardsPort.getCards(filter)).thenReturn(Collections.emptyList());

        List<CardDTO> result = getCardsService.getCards(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(getCardsPort).getCards(filter);
        verify(cardMapper, never()).fromEntityToDTO(any());
    }

    @Test
    void shouldThrowMappingException() {
        GetCardsFilter filter = new GetCardsFilter();
        CardEntity cardEntity = new CardEntity();

        when(getCardsPort.getCards(filter)).thenReturn(List.of(cardEntity));
        when(cardMapper.fromEntityToDTO(cardEntity)).thenThrow(new MappingException("Mapping failed"));

        MappingException exception = assertThrows(MappingException.class, () ->
                getCardsService.getCards(filter)
        );

        assertTrue(exception.getMessage().contains("Error while mapping"));
    }

    @Test
    void shouldThrowDatabaseInteractionExceptionOnIllegalArgumentException() {
        GetCardsFilter filter = new GetCardsFilter();

        when(getCardsPort.getCards(filter)).thenThrow(new IllegalArgumentException("Invalid filter"));

        assertThrows(DatabaseInteractionException.class, () ->
                getCardsService.getCards(filter)
        );
    }

    @Test
    void shouldThrowDatabaseInteractionExceptionOnPersistenceException() {
        GetCardsFilter filter = new GetCardsFilter();

        when(getCardsPort.getCards(filter)).thenThrow(new PersistenceException("Persistence error"));

        assertThrows(DatabaseInteractionException.class, () ->
                getCardsService.getCards(filter)
        );
    }

    @Test
    void shouldThrowDatabaseInteractionExceptionOnDataAccessException() {
        GetCardsFilter filter = new GetCardsFilter();
        DataAccessException dataAccessException = mock(DataAccessException.class);

        when(getCardsPort.getCards(filter)).thenThrow(dataAccessException);

        assertThrows(DatabaseInteractionException.class, () ->
                getCardsService.getCards(filter)
        );
    }

    @Test
    void shouldThrowDatabaseInteractionExceptionOnUnknownException() {
        GetCardsFilter filter = new GetCardsFilter();

        when(getCardsPort.getCards(filter)).thenThrow(new RuntimeException("Unknown error"));

        assertThrows(DatabaseInteractionException.class, () ->
                getCardsService.getCards(filter)
        );
    }
}
