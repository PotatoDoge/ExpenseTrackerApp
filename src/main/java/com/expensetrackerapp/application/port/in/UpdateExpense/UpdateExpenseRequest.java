package com.expensetrackerapp.application.port.in.UpdateExpense;

import com.expensetrackerapp.application.port.BaseExpenseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UpdateExpenseRequest extends BaseExpenseRequest {
}
