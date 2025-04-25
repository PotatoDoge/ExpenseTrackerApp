package com.expensetrackerapp.application.service;

import com.expensetrackerapp.application.port.in.GetExpenses.GetExpensesFilters;
import com.expensetrackerapp.application.port.in.GetExpenses.GetExpensesUseCase;
import com.expensetrackerapp.application.port.out.GetExpensesOutboundPort;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class GetExpensesService implements GetExpensesUseCase<ExpenseDTO, GetExpensesFilters> {

    private final GetExpensesOutboundPort<ExpenseEntity, GetExpensesFilters> getExpensesPort;
    private final ExpenseMapper expenseMapper;

    @Override
    public List<ExpenseDTO> getExpenses(GetExpensesFilters filters) {
        return getExpensesPort.getExpenses(filters).stream().map(expenseMapper::fromEntityToDTO).toList();
    }
}
