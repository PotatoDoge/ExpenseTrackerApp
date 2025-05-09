package com.expensetrackerapp.application.service.Expense;

import com.expensetrackerapp.application.port.in.Expense.SaveExpense.SaveExpenseRequest;
import com.expensetrackerapp.application.port.in.Expense.SaveExpense.SaveExpenseUseCase;
import com.expensetrackerapp.application.port.out.Expense.SaveExpenseOutboundPort;
import com.expensetrackerapp.domain.model.Expense;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import com.expensetrackerapp.shared.exceptions.NullRequestException;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
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
            log.error("Request's body (SaveExpenseRequest) cannot be null");
            throw new NullRequestException("Request's body (SaveExpenseRequest) cannot be null");
        }

        Expense expense;
        try{
            expense = expenseMapper.fromRequestToPojo(saveExpenseRequest);
            log.info("Saving expense: {}", expense);
            ExpenseEntity expenseEntity = saveExpensePort.saveExpense(expense);
            return expenseMapper.fromEntityToDTO(expenseEntity);
        }
        catch (IllegalArgumentException | PersistenceException | DataAccessException |
               NullPointerException | ClassCastException e) {
            log.error("Error occurred while saving expense: {}", e.getClass().getSimpleName(), e);
            throw new DatabaseInteractionException("Error while saving expense: " + e.getClass().getSimpleName());
        }
        catch (MappingException e) {
            throw new MappingException("Error while mapping expense: " + e.getClass().getSimpleName());
        }
        catch (Exception e) {
            log.error("Unexpected error occurred while saving expense", e);
            throw new DatabaseInteractionException("Unhandled error while saving expensea.");
        }
    }
}
