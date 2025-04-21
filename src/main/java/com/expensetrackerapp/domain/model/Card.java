package com.expensetrackerapp.domain.model;

import com.expensetrackerapp.domain.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    private Long id;
    private String name;
    private CardType type;
    private String lastDigits;
    private String bankName;
}