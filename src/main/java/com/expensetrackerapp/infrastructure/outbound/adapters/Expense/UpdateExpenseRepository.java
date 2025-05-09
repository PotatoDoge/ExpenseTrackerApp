package com.expensetrackerapp.infrastructure.outbound.adapters.Expense;

import com.expensetrackerapp.application.port.out.Expense.UpdateExpenseOutboundPort;
import com.expensetrackerapp.domain.model.Expense;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import com.expensetrackerapp.infrastructure.outbound.repositories.ExpenseRepository;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
@Log4j2
public class UpdateExpenseRepository implements UpdateExpenseOutboundPort<ExpenseEntity> {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public ExpenseEntity updateExpense(Expense expense, Long expenseId) {

        Optional<ExpenseEntity> expenseToBeUpdated = expenseRepository.findById(expenseId);
        if(expenseToBeUpdated.isEmpty()) {
            log.error("Expense with id {} not found", expense.getId());
            throw new NotFoundInDatabase("Expense with id " + expense.getId() + " not found in database");
        }

        ExpenseEntity existingEntity = expenseToBeUpdated.get();
        expenseMapper.updateEntity(existingEntity, expenseMapper.fromPojoToEntity(expense));
        expenseRepository.save(existingEntity);

        log.info("Updated expense: {}", existingEntity);
        return existingEntity;
    }
}
