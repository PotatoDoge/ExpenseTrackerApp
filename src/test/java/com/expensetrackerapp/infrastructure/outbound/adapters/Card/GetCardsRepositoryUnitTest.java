package com.expensetrackerapp.infrastructure.outbound.adapters.Card;

import com.expensetrackerapp.application.port.in.Card.GetCards.GetCardsFilter;
import com.expensetrackerapp.domain.enums.CardType;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.repositories.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCardsRepositoryUnitTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private GetCardsRepository getCardsRepository;

    private GetCardsFilter filters;
    private CardEntity cardEntity;

    @BeforeEach
    void setUp() {
        filters = GetCardsFilter.builder()
                .id(1L)
                .name("Visa Gold")
                .type(CardType.CREDIT)
                .lastDigits("1234")
                .bankName("MyBank")
                .build();

        cardEntity = CardEntity.builder()
                .id(1L)
                .name("Visa Gold")
                .type(CardType.CREDIT)
                .lastDigits("1234")
                .bankName("MyBank")
                .build();
    }

    @Test
    void getCards_shouldReturnMatchingCards() {
        // Arrange
        List<CardEntity> mockResult = List.of(cardEntity);
        when(cardRepository.findAllCardsByFilters(
                1L, "Visa Gold", CardType.CREDIT, "1234", "MyBank"
        )).thenReturn(mockResult);

        // Act
        List<CardEntity> result = getCardsRepository.getCards(filters);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Visa Gold", result.getFirst().getName());
        verify(cardRepository, times(1)).findAllCardsByFilters(
                1L, "Visa Gold", CardType.CREDIT, "1234", "MyBank"
        );
    }

    @Test
    void getCards_shouldReturnEmptyList() {
        // Arrange
        when(cardRepository.findAllCardsByFilters(
                1L, "Visa Gold", CardType.CREDIT, "1234", "MyBank"
        )).thenReturn(List.of());

        // Act
        List<CardEntity> result = getCardsRepository.getCards(filters);

        // Assert
        assertEquals(0, result.size());
        verify(cardRepository, times(1)).findAllCardsByFilters(
                1L, "Visa Gold", CardType.CREDIT, "1234", "MyBank"
        );
    }
}
