package com.expensetrackerapp.dto;

import com.expensetrackerapp.domain.enums.CardType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDTO {

    private Long id;
    private String name;
    private CardType type;
    private String lastDigits;
    private String bankName;
}
