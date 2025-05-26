package com.expensetrackerapp.infrastructure.outbound.mappers;

import com.expensetrackerapp.application.port.base.BaseCardRequest;
import com.expensetrackerapp.domain.enums.CardType;
import com.expensetrackerapp.domain.model.Card;
import com.expensetrackerapp.dto.CardDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.shared.exceptions.MappingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardMapperTest {

    private CardMapper cardMapper;

    @BeforeEach
    void setUp() {
        cardMapper = new CardMapper();
    }

    @Test
    void fromRequestToPojo_shouldMapCorrectly() {
        BaseCardRequest request = mock(BaseCardRequest.class);
        when(request.getName()).thenReturn("My Card");
        when(request.getType()).thenReturn(CardType.CREDIT);
        when(request.getLastDigits()).thenReturn("1234");
        when(request.getBankName()).thenReturn("Test Bank");

        Card result = cardMapper.fromRequestToPojo(request);

        assertNotNull(result);
        assertEquals("My Card", result.getName());
        assertEquals(CardType.CREDIT, result.getType());
        assertEquals("1234", result.getLastDigits());
        assertEquals("Test Bank", result.getBankName());
    }

    @Test
    void fromRequestToPojo_shouldThrowMappingException_onError() {
        BaseCardRequest request = mock(BaseCardRequest.class);
        when(request.getName()).thenThrow(new RuntimeException("Mocked failure"));

        MappingException exception = assertThrows(MappingException.class, () ->
                cardMapper.fromRequestToPojo(request)
        );

        assertTrue(exception.getMessage().contains("Error while mapping BaseCardRequest to object"));
    }

    @Test
    void fromPojoToEntity_shouldMapCorrectly() {
        Card card = Card.builder()
                .id(1L)
                .name("Card 1")
                .type(CardType.DEBIT)
                .lastDigits("5678")
                .bankName("Bank X")
                .build();

        CardEntity result = cardMapper.fromPojoToEntity(card);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Card 1", result.getName());
        assertEquals(CardType.DEBIT, result.getType());
        assertEquals("5678", result.getLastDigits());
        assertEquals("Bank X", result.getBankName());
    }

    @Test
    void fromPojoToEntity_shouldThrowMappingException_onError() {
        Card card = mock(Card.class);
        when(card.getName()).thenThrow(new RuntimeException("Mocked exception"));

        MappingException exception = assertThrows(MappingException.class, () ->
                cardMapper.fromPojoToEntity(card)
        );

        assertTrue(exception.getMessage().contains("Error while mapping Card to entity"));
    }

    @Test
    void fromEntityToDTO_shouldMapCorrectly() {
        CardEntity entity = CardEntity.builder()
                .id(2L)
                .name("Virtual Card")
                .type(CardType.CREDIT)
                .lastDigits("9999")
                .bankName("Bank Y")
                .build();

        CardDTO result = cardMapper.fromEntityToDTO(entity);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Virtual Card", result.getName());
        assertEquals(CardType.CREDIT, result.getType());
        assertEquals("9999", result.getLastDigits());
        assertEquals("Bank Y", result.getBankName());
    }

    @Test
    void fromEntityToDTO_shouldThrowMappingException_onError() {
        CardEntity entity = mock(CardEntity.class);
        when(entity.getId()).thenThrow(new RuntimeException("Mapping fail"));

        MappingException exception = assertThrows(MappingException.class, () ->
                cardMapper.fromEntityToDTO(entity)
        );

        assertTrue(exception.getMessage().contains("Error while mapping entity to dto"));
    }

    @Test
    void fromEntityToPOJO_shouldMapCorrectly() {
        CardEntity entity = CardEntity.builder()
                .id(3L)
                .name("Gold Card")
                .type(CardType.DEBIT)
                .lastDigits("8888")
                .bankName("Bank Z")
                .build();

        Card result = cardMapper.fromEntityToPOJO(entity);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Gold Card", result.getName());
        assertEquals(CardType.DEBIT, result.getType());
        assertEquals("8888", result.getLastDigits());
        assertEquals("Bank Z", result.getBankName());
    }

    @Test
    void updateEntity_shouldCopyFieldsCorrectly() {
        CardEntity existing = new CardEntity();
        CardEntity updated = CardEntity.builder()
                .name("Updated Name")
                .type(CardType.CREDIT)
                .lastDigits("1111")
                .bankName("Updated Bank")
                .build();

        cardMapper.updateEntity(existing, updated);

        assertEquals("Updated Name", existing.getName());
        assertEquals(CardType.CREDIT, existing.getType());
        assertEquals("1111", existing.getLastDigits());
        assertEquals("Updated Bank", existing.getBankName());
    }
}
