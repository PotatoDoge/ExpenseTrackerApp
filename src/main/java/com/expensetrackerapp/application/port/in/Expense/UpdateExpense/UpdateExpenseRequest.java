package com.expensetrackerapp.application.port.in.Expense.UpdateExpense;

import com.expensetrackerapp.application.port.base.BaseExpenseRequest;
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
