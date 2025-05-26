package com.expensetrackerapp.infrastructure.outbound.adapters.Card;

import com.expensetrackerapp.domain.enums.CardType;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.repositories.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCardByIdRepositoryUnitTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private GetCardByIdRepository getCardByIdRepository;

    private CardEntity cardEntity;

    @BeforeEach
    void setUp() {
        cardEntity = CardEntity.builder()
                .id(1L)
                .name("Visa Gold")
                .type(CardType.CREDIT)
                .lastDigits("1234")
                .bankName("MyBank")
                .build();
    }

    @Test
    void getCardById_shouldReturnCardEntity() {
        // Arrange
        when(cardRepository.findById(1L)).thenReturn(Optional.of(cardEntity));

        // Act
        Optional<CardEntity> result = getCardByIdRepository.getCardById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Visa Gold", result.get().getName());
        verify(cardRepository, times(1)).findById(1L);
    }

    @Test
    void getCardById_shouldReturnEmptyOptional() {
        // Arrange
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        Optional<CardEntity> result = getCardByIdRepository.getCardById(2L);

        // Assert
        assertFalse(result.isPresent());
        verify(cardRepository, times(1)).findById(2L);
    }
}
