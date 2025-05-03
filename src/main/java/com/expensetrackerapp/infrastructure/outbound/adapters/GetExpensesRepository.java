package com.expensetrackerapp.infrastructure.outbound.adapters;

import com.expensetrackerapp.application.port.in.GetExpenses.GetExpensesFilters;
import com.expensetrackerapp.application.port.out.GetExpensesOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.repositories.ExpenseRepository;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
@Log4j2
public class GetExpensesRepository implements GetExpensesOutboundPort<ExpenseEntity, GetExpensesFilters> {

    private final ExpenseRepository expenseRepository;

    @Override
    public List<ExpenseEntity> getExpenses(GetExpensesFilters filters) {
        List<ExpenseEntity> expenseEntities =
                expenseRepository.findAllExpensesByFilters(
                        filters.getExpenseId(),
                        filters.getExpenseName(),
                        filters.getDescription(),
                        filters.getAmount(),
                        filters.getCurrency(),
                        filters.getExpenseDate(),
                        filters.getRequiresInvoice(),
                        filters.getInstallments(),
                        filters.getVendor(),
                        filters.getLocation(),
                        filters.getPaidInFull());
        log.info("Fetched expenses: {}", expenseEntities.size());
        return expenseEntities;
    }
}
