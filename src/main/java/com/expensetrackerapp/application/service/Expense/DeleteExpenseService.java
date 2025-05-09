package com.expensetrackerapp.application.service.Expense;

import com.expensetrackerapp.application.port.in.Expense.DeleteExpense.DeleteExpenseUseCase;
import com.expensetrackerapp.application.port.out.Expense.DeleteExpenseOutboundPort;
import com.expensetrackerapp.application.port.out.Expense.GetExpenseByIdOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class DeleteExpenseService implements DeleteExpenseUseCase {

    private final DeleteExpenseOutboundPort deleteExpensePort;
    private final GetExpenseByIdOutboundPort<ExpenseEntity> getExpenseByIdPort;

    @Override
    public void deleteExpense(Long expenseId) {
        try{
            Optional<ExpenseEntity> existingExpense = getExpenseByIdPort.getExpenseById(expenseId);

            if(existingExpense.isEmpty()) {
                log.warn("Expense with id: {} does not exist", expenseId);
                throw new NotFoundInDatabase("Expense with id: " + expenseId + " does not exist");
            }
            log.info("Deleting expense with id: {}", expenseId);
            deleteExpensePort.deleteExpense(expenseId);
        }
        catch (NotFoundInDatabase e) {
            throw e;
        }
        catch (IllegalArgumentException | PersistenceException | DataAccessException |
               NullPointerException | ClassCastException e) {
            log.error("Error occurred while deleting expense with id: {}", expenseId, e);
            throw new DatabaseInteractionException("Error occurred while deleting expense with id: " + expenseId);
        }
        catch (Exception e) {
            log.error("Unexpected error occurred while deleting expense with id: {}", expenseId, e);
            throw new DatabaseInteractionException("Unexpected error occurred while deleting expense with id: " + expenseId);
        }
    }
}
