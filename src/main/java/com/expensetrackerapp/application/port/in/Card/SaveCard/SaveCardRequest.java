package com.expensetrackerapp.application.port.in.Card.SaveCard;

import com.expensetrackerapp.application.port.base.BaseCardRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SaveCardRequest extends BaseCardRequest {
}
