package com.expensetrackerapp.application.port.in.Card.GetCards;

import com.expensetrackerapp.domain.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCardsFilter {

    private Long id;
    private String name;
    private CardType type;
    private String lastDigits;
    private String bankName;

}
