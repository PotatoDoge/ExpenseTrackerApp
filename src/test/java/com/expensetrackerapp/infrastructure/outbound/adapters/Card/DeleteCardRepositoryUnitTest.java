package com.expensetrackerapp.infrastructure.outbound.adapters.Card;

import com.expensetrackerapp.infrastructure.outbound.repositories.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCardRepositoryUnitTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private DeleteCardRepository deleteCardRepository;

    @Test
    void deleteCard_shouldCallRepositoryDeleteById() {
        // Arrange
        Long cardId = 1L;

        // Act
        deleteCardRepository.deleteCard(cardId);

        // Assert
        verify(cardRepository, times(1)).deleteById(cardId);
    }
}
