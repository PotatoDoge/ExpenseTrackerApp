package com.expensetrackerapp.application.service.Expense;

import com.expensetrackerapp.application.port.in.Expense.UpdateExpense.UpdateExpenseRequest;
import com.expensetrackerapp.application.port.in.Expense.UpdateExpense.UpdateExpenseUseCase;
import com.expensetrackerapp.application.port.out.Expense.UpdateExpenseOutboundPort;
import com.expensetrackerapp.domain.model.Expense;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
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
public class UpdateExpenseService implements UpdateExpenseUseCase<ExpenseDTO> {

    private final UpdateExpenseOutboundPort<ExpenseEntity> updateExpenseOutboundPort;
    private final ExpenseMapper expenseMapper;

    @Override
    public ExpenseDTO updateExpense(UpdateExpenseRequest updateExpenseRequest, Long expenseId) {
        if (Objects.isNull(updateExpenseRequest)) {
            log.error("Request's body (UpdateExpenseRequest) cannot be null");
            throw new NullRequestException("Request's body (UpdateExpenseRequest) cannot be null");
        }

        Expense expense;
        try{
            expense = expenseMapper.fromRequestToPojo(updateExpenseRequest);
            log.info("Updating expense: {}", expense);
            ExpenseEntity expenseEntity = updateExpenseOutboundPort.updateExpense(expense, expenseId);
            return expenseMapper.fromEntityToDTO(expenseEntity);
        }
        catch (NotFoundInDatabase e){
            throw e;
        }
        catch (IllegalArgumentException | PersistenceException | DataAccessException |
               NullPointerException | ClassCastException e) {
            log.error("Error occurred while updating expense: {}", e.getClass().getSimpleName(), e);
            throw new DatabaseInteractionException("Error while updating expense: " + e.getClass().getSimpleName());
        }
        catch (MappingException e) {
            throw new MappingException("Error while mapping expense: " + e.getClass().getSimpleName());
        }
        catch (Exception e) {
            log.error("Unexpected error occurred while updating expense", e);
            throw new DatabaseInteractionException("Unhandled error while updating expense.");
        }
    }
}
