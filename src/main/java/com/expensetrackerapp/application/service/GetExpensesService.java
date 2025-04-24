package com.expensetrackerapp.application.service;

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
public class GetExpensesService implements GetExpensesUseCase<ExpenseDTO> {

    private final GetExpensesOutboundPort<ExpenseEntity> getExpensesPort;
    private final ExpenseMapper expenseMapper;

    @Override
    public List<ExpenseDTO> getExpenses() {
        return getExpensesPort.getExpenses().stream().map(expenseMapper::fromEntityToDTO).toList();
    }
}
