package com.expensetrackerapp.application.port.base;

import com.expensetrackerapp.domain.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class BaseCardRequest {
    private String name;
    private CardType type;
    private String lastDigits;
    private String bankName;
}
