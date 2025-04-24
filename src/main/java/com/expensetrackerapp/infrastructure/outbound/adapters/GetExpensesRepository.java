package com.expensetrackerapp.infrastructure.outbound.adapters;

import com.expensetrackerapp.application.port.out.GetExpensesOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.repositories.ExpenseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Log4j2
public class GetExpensesRepository implements GetExpensesOutboundPort<ExpenseEntity> {

    private final ExpenseRepository expenseRepository;

    @Override
    public List<ExpenseEntity> getExpenses() {
        List<ExpenseEntity> expenseEntities = expenseRepository.findAll();
        log.info("Found {} expenses", expenseEntities.size());
        return expenseEntities;
    }
}
