package com.expensetrackerapp.infrastructure.inbound.controller.rest;

import com.expensetrackerapp.application.port.in.Card.DeleteCard.DeleteCardUseCase;
import com.expensetrackerapp.application.port.in.Card.GetCards.GetCardsFilter;
import com.expensetrackerapp.application.port.in.Card.GetCards.GetCardsUseCase;
import com.expensetrackerapp.application.port.in.Card.SaveCard.SaveCardRequest;
import com.expensetrackerapp.application.port.in.Card.SaveCard.SaveCardUseCase;
import com.expensetrackerapp.domain.enums.CardType;
import com.expensetrackerapp.dto.CardDTO;
import com.expensetrackerapp.shared.CustomResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cards")
@AllArgsConstructor
@Log4j2
public class CardController {

    private final SaveCardUseCase<CardDTO> saveCardUseCase;
    private final GetCardsUseCase<CardDTO, GetCardsFilter> getCardsUseCase;
    private final DeleteCardUseCase deleteCardUseCase;

    @PostMapping
    public ResponseEntity<CustomResponse> saveCard(@RequestBody SaveCardRequest saveCardRequest) {
        log.info("Received request to save card: name={}",
                saveCardRequest.getName());
        CardDTO savedCard = saveCardUseCase.saveCard(saveCardRequest);
        CustomResponse response = CustomResponse
                .builder()
                .timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now()))
                .message("Card saved successfully")
                .data(Map.of("card", savedCard)).build();
        log.info("Card saved successfully with ID: {}", savedCard.getId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<CustomResponse> getCards(
            @RequestParam(required = false) Long cardId,
            @RequestParam(required = false) String cardName,
            @RequestParam(required = false) CardType type,
            @RequestParam(required = false) String lastDigits,
            @RequestParam(required = false) String bankName
    ) {
        GetCardsFilter getCardsFilter = GetCardsFilter
                .builder().id(cardId).name(cardName).type(type).lastDigits(lastDigits).bankName(bankName).build();
        log.info("Received request to get all cards with the following filters: {}", getCardsFilter);
        List<CardDTO> cards = getCardsUseCase.getCards(getCardsFilter);
        CustomResponse response = CustomResponse.builder()
                .timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now()))
                .message("Cards fetched successfully")
                .data(Map.of("cards", cards))
                .build();
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("{cardId}")
    public ResponseEntity<CustomResponse> deleteCard(@PathVariable Long cardId) {
        log.info("Received request to delete card with ID: {}", cardId);
        deleteCardUseCase.deleteCard(cardId);
        log.info("Card deleted successfully with ID: {}", cardId);
        CustomResponse response = CustomResponse.builder()
                .timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now()))
                .message("Card deleted successfully")
                .build();
        return ResponseEntity.status(204).body(response);
    }
}
