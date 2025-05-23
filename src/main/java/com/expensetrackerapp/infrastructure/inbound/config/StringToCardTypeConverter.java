package com.expensetrackerapp.infrastructure.inbound.config;

import com.expensetrackerapp.domain.enums.CardType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCardTypeConverter implements Converter<String, CardType> {

    @Override
    public CardType convert(String source) {
        return CardType.fromString(source);
    }
}