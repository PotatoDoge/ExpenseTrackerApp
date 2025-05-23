package com.expensetrackerapp.infrastructure.inbound.controller.rest;

import com.expensetrackerapp.application.port.in.Card.SaveCard.SaveCardRequest;
import com.expensetrackerapp.application.port.in.Card.SaveCard.SaveCardUseCase;
import com.expensetrackerapp.dto.CardDTO;
import com.expensetrackerapp.shared.CustomResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/cards")
@AllArgsConstructor
@Log4j2
public class CardController {

    private final SaveCardUseCase<CardDTO> saveCardUseCase;

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
}
