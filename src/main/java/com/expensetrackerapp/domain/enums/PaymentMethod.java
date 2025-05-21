package com.expensetrackerapp.domain.enums;

import com.expensetrackerapp.shared.exceptions.InvalidEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum PaymentMethod {
    CASH("cash"),
    CARD("card"),
    BANK_TRANSFER("bank_transfer"),
    MOBILE_PAYMENT("mobile_payment"),
    OTHER("other");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    /**
     * Custom deserialization
     * @param key - enum value
     * @return enum value
     * @throws InvalidEnum when an enum value is invalid (not registered in this class)
     */
    @JsonCreator
    public static PaymentMethod fromString(String key) {
        return Arrays.stream(values())
                .filter(e -> e.value.equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(() -> new InvalidEnum("Invalid payment method"));
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
