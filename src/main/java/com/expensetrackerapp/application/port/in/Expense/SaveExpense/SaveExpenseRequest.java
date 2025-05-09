package com.expensetrackerapp.application.port.in.Expense.SaveExpense;

import com.expensetrackerapp.application.port.base.BaseExpenseRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SaveExpenseRequest extends BaseExpenseRequest { }