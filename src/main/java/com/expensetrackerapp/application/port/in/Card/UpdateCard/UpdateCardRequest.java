package com.expensetrackerapp.application.port.in.Card.UpdateCard;

import com.expensetrackerapp.application.port.base.BaseCardRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UpdateCardRequest extends BaseCardRequest {
}
