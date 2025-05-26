package com.expensetrackerapp.infrastructure.outbound.adapters.Card;

import com.expensetrackerapp.domain.enums.CardType;
import com.expensetrackerapp.domain.model.Card;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CardMapper;
import com.expensetrackerapp.infrastructure.outbound.repositories.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SaveCardRepositoryUnitTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private SaveCardRepository saveCardRepository;

    private Card card;
    private CardEntity cardEntity;

    @BeforeEach
    void setUp() {
        card = Card.builder()
                .id(1L)
                .name("My Visa")
                .type(CardType.CREDIT)
                .lastDigits("1234")
                .bankName("Chase")
                .build();

        cardEntity = CardEntity.builder()
                .id(1L)
                .name("My Visa")
                .type(CardType.CREDIT)
                .lastDigits("1234")
                .bankName("Chase")
                .build();
    }

    @Test
    void saveCard_shouldReturnSavedCardEntity() {
        // Arrange
        when(cardMapper.fromPojoToEntity(card)).thenReturn(cardEntity);
        when(cardRepository.save(cardEntity)).thenReturn(cardEntity);

        // Act
        CardEntity result = saveCardRepository.saveCard(card);

        // Assert
        assertNotNull(result);
        assertEquals("My Visa", result.getName());
        verify(cardMapper, times(1)).fromPojoToEntity(card);
        verify(cardRepository, times(1)).save(cardEntity);
    }
}
