package com.expensetrackerapp.domain.enums;

import com.expensetrackerapp.shared.exceptions.InvalidEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum CardType {
    CREDIT("credit"),
    DEBIT("debit"),
    PREPAID("prepaid");

    private final String value;

    CardType(String value) {
        this.value = value;
    }

    /**
     * Custom deserialization
     * @param key - enum value
     * @return enum value
     * @throws InvalidEnum when an enum value is invalid (not registered in this class)
     */
    @JsonCreator
    public static CardType fromString(String key) {
        return Arrays.stream(values())
                .filter(e -> e.value.equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(() -> new InvalidEnum("Invalid card type: " + key));
    }

    /**
     * Custom serialization
     * @return enum value
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}
