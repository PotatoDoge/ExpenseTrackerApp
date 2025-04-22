package com.expensetrackerapp.application.service;

import com.expensetrackerapp.application.port.in.SaveExpense.SaveExpenseRequest;
import com.expensetrackerapp.application.port.in.SaveExpense.SaveExpenseUseCase;
import com.expensetrackerapp.application.port.out.SaveExpenseOutboundPort;
import com.expensetrackerapp.domain.model.Expense;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import com.expensetrackerapp.shared.exceptions.NullRequestException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
@Log4j2
public class SaveExpenseService implements SaveExpenseUseCase<ExpenseDTO> {

    private final SaveExpenseOutboundPort<ExpenseEntity> saveExpensePort;
    private final ExpenseMapper expenseMapper;

    @Override
    public ExpenseDTO saveExpense(SaveExpenseRequest saveExpenseRequest) {
        if (Objects.isNull(saveExpenseRequest)) {
            throw new NullRequestException("Request's body (SaveExpenseRequest) cannot be null");
        }
        Expense expense = expenseMapper.fromRequestToPojo(saveExpenseRequest);
        log.info("Saving expense: {}", expense);
        ExpenseEntity expenseEntity = saveExpensePort.saveExpense(expense);
        return expenseMapper.fromEntityToDTO(expenseEntity);
    }
}
