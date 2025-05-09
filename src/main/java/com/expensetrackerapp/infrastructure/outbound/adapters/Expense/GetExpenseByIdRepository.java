package com.expensetrackerapp.infrastructure.outbound.adapters.Expense;

import com.expensetrackerapp.application.port.out.Expense.GetExpenseByIdOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.repositories.ExpenseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
@Log4j2
public class GetExpenseByIdRepository implements GetExpenseByIdOutboundPort<ExpenseEntity> {

    private final ExpenseRepository expenseRepository;

    @Override
    public Optional<ExpenseEntity> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }
}
