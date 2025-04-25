package com.expensetrackerapp.infrastructure.outbound.adapters;

import com.expensetrackerapp.application.port.out.SaveExpenseOutboundPort;
import com.expensetrackerapp.domain.model.Expense;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import com.expensetrackerapp.infrastructure.outbound.repositories.ExpenseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
@Log4j2
public class SaveExpenseRepository implements SaveExpenseOutboundPort<ExpenseEntity> {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public ExpenseEntity saveExpense(Expense expense) {
        expense.setExpenseDate(LocalDate.now());
        ExpenseEntity savedExpense = expenseRepository.save(expenseMapper.fromPojoToEntity(expense));
        log.info("Saved expense: {}", savedExpense);
        return savedExpense;
    }
}
