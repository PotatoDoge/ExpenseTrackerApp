package com.expensetrackerapp.application.service;

import com.expensetrackerapp.application.port.out.Expense.DeleteExpenseOutboundPort;
import com.expensetrackerapp.application.port.out.Expense.GetExpenseByIdOutboundPort;
import com.expensetrackerapp.application.service.Expense.DeleteExpenseService;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteExpenseServiceUnitTest {

    @Mock
    private DeleteExpenseOutboundPort deleteExpensePort;

    @Mock
    private GetExpenseByIdOutboundPort<ExpenseEntity> getExpenseByIdPort;

    @InjectMocks
    private DeleteExpenseService deleteExpenseService;

    private static final Long EXPENSE_ID = 1L;
    private static final ExpenseEntity EXPENSE_ENTITY = new ExpenseEntity();

    @BeforeEach
    void setUp() {
        EXPENSE_ENTITY.setId(EXPENSE_ID);
    }

    @Test
    void testDeleteExpense_Success() {
        // Arrange
        when(getExpenseByIdPort.getExpenseById(EXPENSE_ID)).thenReturn(Optional.of(EXPENSE_ENTITY));

        // Act
        deleteExpenseService.deleteExpense(EXPENSE_ID);

        // Assert
        verify(deleteExpensePort, times(1)).deleteExpense(EXPENSE_ID);
    }

    @Test
    void testDeleteExpense_ExpenseNotFound() {
        // Arrange
        when(getExpenseByIdPort.getExpenseById(EXPENSE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundInDatabase exception = assertThrows(NotFoundInDatabase.class, () -> {
            deleteExpenseService.deleteExpense(EXPENSE_ID);
        });
        assertEquals("Expense with id: " + EXPENSE_ID + " does not exist", exception.getMessage());
    }

    @Test
    void testDeleteExpense_DatabaseInteractionException() {
        // Arrange
        when(getExpenseByIdPort.getExpenseById(EXPENSE_ID)).thenReturn(Optional.of(EXPENSE_ENTITY));
        doThrow(new IllegalArgumentException("Illegal argument")).when(deleteExpensePort).deleteExpense(EXPENSE_ID);

        // Act & Assert
        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            deleteExpenseService.deleteExpense(EXPENSE_ID);
        });
        assertTrue(exception.getMessage().contains("Error occurred while deleting expense with id: " + EXPENSE_ID));
    }

    @Test
    void testDeleteExpense_UnhandledException() {
        // Arrange
        when(getExpenseByIdPort.getExpenseById(EXPENSE_ID)).thenReturn(Optional.of(EXPENSE_ENTITY));
        doThrow(new RuntimeException("Unhandled exception")).when(deleteExpensePort).deleteExpense(EXPENSE_ID);

        // Act & Assert
        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            deleteExpenseService.deleteExpense(EXPENSE_ID);
        });
        assertTrue(exception.getMessage().contains("Unexpected error occurred while deleting expense with id: " + EXPENSE_ID));
    }

    @Test
    void testDeleteExpense_ThrowsPersistenceException() {
        // Arrange
        when(getExpenseByIdPort.getExpenseById(EXPENSE_ID)).thenReturn(Optional.of(EXPENSE_ENTITY));
        doThrow(new PersistenceException("Persistence issue")).when(deleteExpensePort).deleteExpense(EXPENSE_ID);

        // Act & Assert
        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            deleteExpenseService.deleteExpense(EXPENSE_ID);
        });
        assertTrue(exception.getMessage().contains("Error occurred while deleting expense with id: " + EXPENSE_ID));
    }
}
