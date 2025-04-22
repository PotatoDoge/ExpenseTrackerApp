package com.expensetrackerapp.application.service;

import com.expensetrackerapp.application.port.in.SaveExpense.SaveExpenseRequest;
import com.expensetrackerapp.application.port.in.SaveExpense.SaveExpenseUseCase;
import com.expensetrackerapp.application.port.out.SaveExpenseOutboundPort;
import com.expensetrackerapp.domain.model.Expense;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class SaveExpenseService implements SaveExpenseUseCase<ExpenseDTO> {

    private final SaveExpenseOutboundPort<ExpenseEntity> saveExpensePort;
    private final ObjectMapper objectMapper;

    @Override
    public ExpenseDTO saveExpense(SaveExpenseRequest saveExpenseRequest) {
        Expense expense = objectMapper.convertValue(saveExpenseRequest, Expense.class);
        log.info("Saving expense: {}", expense);
        ExpenseEntity expenseEntity = saveExpensePort.saveExpense(expense);
        return objectMapper.convertValue(expenseEntity, ExpenseDTO.class);
    }
}
