package com.expensetrackerapp.infrastructure.outbound.mappers;

import com.expensetrackerapp.application.port.base.BaseCardRequest;
import com.expensetrackerapp.domain.model.Card;
import com.expensetrackerapp.dto.CardDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.shared.exceptions.MappingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CardMapper implements ExtendedMapper<Card, CardEntity, CardDTO, BaseCardRequest>{

    @Override
    public Card fromRequestToPojo(BaseCardRequest baseCardRequest) {
        try{
            return Card.builder()
                    .name(baseCardRequest.getName())
                    .type(baseCardRequest.getType())
                    .lastDigits(baseCardRequest.getLastDigits())
                    .bankName(baseCardRequest.getBankName())
                    .build();
        }
        catch (Exception e) {
            log.error("Error occurred while mapping BaseCardRequest to object: {}", e.getMessage(), e);
            throw new MappingException("Error while mapping BaseCardRequest to object: " + e);
        }
    }

    @Override
    public void updateEntity(CardEntity existingEntity, CardEntity newEntity) {
        existingEntity.setName(newEntity.getName());
        existingEntity.setType(newEntity.getType());
        existingEntity.setLastDigits(newEntity.getLastDigits());
        existingEntity.setBankName(newEntity.getBankName());
    }

    @Override
    public CardEntity fromPojoToEntity(Card card) {
        try{
            return CardEntity.builder()
                    .id(card.getId())
                    .name(card.getName())
                    .type(card.getType())
                    .lastDigits(card.getLastDigits())
                    .bankName(card.getBankName())
                    .build();
        }
        catch (Exception e) {
            log.error("Error occurred while mapping Card pojo to entity: {}", e.getMessage(), e);
            throw new MappingException("Error while mapping Card to entity: " + e);
        }
    }

    @Override
    public CardDTO fromEntityToDTO(CardEntity cardEntity) {
        try{
            return CardDTO.builder()
                    .id(cardEntity.getId())
                    .name(cardEntity.getName())
                    .type(cardEntity.getType())
                    .lastDigits(cardEntity.getLastDigits())
                    .bankName(cardEntity.getBankName())
                    .build();
        }
        catch (Exception e) {
            log.error("Error occurred while mapping entity to dto: {}", e.getMessage(), e);
            throw new MappingException("Error while mapping entity to dto: " + e);
        }
    }

    @Override
    public Card fromEntityToPOJO(CardEntity cardEntity) {
        return Card.builder()
                .id(cardEntity.getId())
                .name(cardEntity.getName())
                .lastDigits(cardEntity.getLastDigits())
                .bankName(cardEntity.getBankName())
                .type(cardEntity.getType())
                .build();
    }
}
