package com.expensetrackerapp.application.service;

import com.expensetrackerapp.application.port.in.GetExpenses.GetExpensesFilters;
import com.expensetrackerapp.application.port.in.GetExpenses.GetExpensesUseCase;
import com.expensetrackerapp.application.port.out.GetExpensesOutboundPort;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
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
        try{
            return getExpensesPort.getExpenses(filters).stream().map(expenseMapper::fromEntityToDTO).toList();
        }
        catch (MappingException e) {
            throw new MappingException("Error while mapping expense from entity to DTO: " + e.getClass().getSimpleName());
        }
        catch (IllegalArgumentException | PersistenceException | DataAccessException |
               NullPointerException | ClassCastException e) {
            log.error("Error occurred while fetching expenses: {} - {}", e.getClass().getSimpleName(), e.getMessage(), e);
            throw new DatabaseInteractionException("Error while fetching expenses: " + e.getClass().getSimpleName());
        }
        catch (Exception e) {
            log.error("Error occurred while fetching expenses: {} - {}", e.getClass().getSimpleName(), e.getMessage(), e);
            throw new DatabaseInteractionException("Unhandled error while fetching all entities.");
        }
    }
}
