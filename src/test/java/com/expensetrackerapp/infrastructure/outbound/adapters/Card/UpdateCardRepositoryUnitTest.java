package com.expensetrackerapp.infrastructure.outbound.adapters.Card;

import com.expensetrackerapp.domain.enums.CardType;
import com.expensetrackerapp.domain.model.Card;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CardMapper;
import com.expensetrackerapp.infrastructure.outbound.repositories.CardRepository;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
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
public class UpdateCardRepositoryUnitTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private UpdateCardRepository updateCardRepository;

    private Card cardToUpdate;
    private CardEntity existingEntity;
    private CardEntity updatedEntity;

    @BeforeEach
    void setUp() {
        cardToUpdate = Card.builder()
                .id(1L)
                .name("Updated Visa")
                .type(CardType.DEBIT)
                .lastDigits("5678")
                .bankName("Wells Fargo")
                .build();

        existingEntity = CardEntity.builder()
                .id(1L)
                .name("Old Visa")
                .type(CardType.CREDIT)
                .lastDigits("1234")
                .bankName("Chase")
                .build();

        updatedEntity = CardEntity.builder()
                .id(1L)
                .name("Updated Visa")
                .type(CardType.DEBIT)
                .lastDigits("5678")
                .bankName("Wells Fargo")
                .build();
    }

    @Test
    void updateCard_shouldUpdateAndReturnCardEntity() {
        // Arrange
        when(cardRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(cardMapper.fromPojoToEntity(cardToUpdate)).thenReturn(updatedEntity);

        doNothing().when(cardMapper).updateEntity(existingEntity, updatedEntity);
        when(cardRepository.save(existingEntity)).thenReturn(existingEntity);

        // Act
        CardEntity result = updateCardRepository.updateCard(cardToUpdate, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(existingEntity.getName(), result.getName());
        verify(cardRepository).findById(1L);
        verify(cardMapper).updateEntity(existingEntity, updatedEntity);
        verify(cardRepository).save(existingEntity);
    }

    @Test
    void updateCard_shouldThrowNotFoundInDatabase_whenCardDoesNotExist() {
        // Arrange
        when(cardRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundInDatabase exception = assertThrows(NotFoundInDatabase.class, () ->
                updateCardRepository.updateCard(cardToUpdate, 999L));

        assertEquals("Card with id 999 not found in database", exception.getMessage());
        verify(cardRepository).findById(999L);
        verifyNoMoreInteractions(cardMapper);
        verify(cardRepository, never()).save(any());
    }
}
