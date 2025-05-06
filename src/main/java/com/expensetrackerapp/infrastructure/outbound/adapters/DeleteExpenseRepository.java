package com.expensetrackerapp.infrastructure.outbound.adapters;

import com.expensetrackerapp.application.port.out.DeleteExpenseOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.repositories.ExpenseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class DeleteExpenseRepository implements DeleteExpenseOutboundPort {

    private final ExpenseRepository expenseRepository;

    @Override
    public void deleteExpense(Long expenseId) {
        expenseRepository.deleteById(expenseId);
        log.info("Expense with id: {} deleted successfully", expenseId);
    }
}
