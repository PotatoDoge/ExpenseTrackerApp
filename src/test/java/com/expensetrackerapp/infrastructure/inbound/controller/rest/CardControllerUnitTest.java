package com.expensetrackerapp.infrastructure.inbound.controller.rest;

import com.expensetrackerapp.application.port.in.Card.DeleteCard.DeleteCardUseCase;
import com.expensetrackerapp.application.port.in.Card.GetCards.GetCardsFilter;
import com.expensetrackerapp.application.port.in.Card.GetCards.GetCardsUseCase;
import com.expensetrackerapp.application.port.in.Card.SaveCard.SaveCardRequest;
import com.expensetrackerapp.application.port.in.Card.SaveCard.SaveCardUseCase;
import com.expensetrackerapp.application.port.in.Card.UpdateCard.UpdateCardRequest;
import com.expensetrackerapp.application.port.in.Card.UpdateCard.UpdateCardUseCase;
import com.expensetrackerapp.domain.enums.CardType;
import com.expensetrackerapp.dto.CardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CardControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private SaveCardUseCase<CardDTO> saveCardUseCase;

    @Mock
    private GetCardsUseCase<CardDTO, GetCardsFilter> getCardsUseCase;

    @Mock
    private DeleteCardUseCase deleteCardUseCase;

    @Mock
    private UpdateCardUseCase<CardDTO> updateCardUseCase;

    @InjectMocks
    private CardController cardController;

    private CardDTO cardDTO;

    private String readJson(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get("src/test/resources/" + path)));
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
        cardDTO = CardDTO.builder()
                .id(1L)
                .name("Visa Gold")
                .type(CardType.CREDIT)
                .bankName("MyBank")
                .lastDigits("1234")
                .build();
    }

    @Test
    void testSaveCardSuccess() throws Exception {
        String saveCardRequestAsJSON = readJson("messages/Card/SaveCardRequest.json");

        when(saveCardUseCase.saveCard(any(SaveCardRequest.class)))
                .thenReturn(cardDTO);

        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saveCardRequestAsJSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Card saved successfully"))
                .andExpect(jsonPath("$.data.card.id").value("1"))
                .andExpect(jsonPath("$.data.card.name").value("Visa Gold"));
    }

    @Test
    void testGetCardsSuccess() throws Exception {
        when(getCardsUseCase.getCards(any(GetCardsFilter.class)))
                .thenReturn(List.of(cardDTO));

        mockMvc.perform(get("/cards")
                        .param("cardName", "Visa Gold"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Cards fetched successfully"))
                .andExpect(jsonPath("$.data.cards[0].name").value("Visa Gold"));
    }

    @Test
    void testDeleteCardSuccess() throws Exception {
        doNothing().when(deleteCardUseCase).deleteCard(1L);

        mockMvc.perform(delete("/cards/{cardId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("Card deleted successfully"));

        verify(deleteCardUseCase, times(1)).deleteCard(1L);
    }

    @Test
    void testUpdateCardSuccess() throws Exception {
        String updateCardRequestAsJSON = readJson("messages/Card/UpdateCardRequest.json");

        CardDTO updatedCardDTO = CardDTO.builder()
                .id(1L)
                .name("Visa Platinum")
                .type(CardType.CREDIT)
                .bankName("UpdatedBank")
                .lastDigits("5678")
                .build();

        when(updateCardUseCase.updateCard(any(UpdateCardRequest.class), eq(1L)))
                .thenReturn(updatedCardDTO);

        mockMvc.perform(put("/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateCardRequestAsJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Card updated successfully"))
                .andExpect(jsonPath("$.data.card.name").value("Visa Platinum"))
                .andExpect(jsonPath("$.data.card.lastDigits").value("5678"));
    }
}
