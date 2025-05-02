package com.expensetrackerapp.application.port.in.SaveExpense;

import com.expensetrackerapp.application.port.BaseExpenseRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SaveExpenseRequest extends BaseExpenseRequest { }