package com.expensetrackerapp.infrastructure.outbound.adapters.Expense;

import com.expensetrackerapp.infrastructure.outbound.repositories.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteExpenseRepositoryUnitTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private DeleteExpenseRepository deleteExpenseRepository;

    private static final Long EXPENSE_ID = 1L;

    @Test
    void testDeleteExpense_Success() {
        // Act
        deleteExpenseRepository.deleteExpense(EXPENSE_ID);

        // Assert
        verify(expenseRepository, times(1)).deleteById(EXPENSE_ID);
    }

    @Test
    void testDeleteExpense_DataAccessException() {
        // Arrange
        doThrow(new DataAccessException("Data access error") {}).when(expenseRepository).deleteById(EXPENSE_ID);

        // Act & Assert
        try {
            deleteExpenseRepository.deleteExpense(EXPENSE_ID);
        } catch (DataAccessException e) {
            assertEquals("Data access error", e.getMessage());
        }
    }

    @Test
    void testDeleteExpense_UnexpectedException() {
        // Arrange
        doThrow(new RuntimeException("Unexpected error")).when(expenseRepository).deleteById(EXPENSE_ID);

        // Act & Assert
        try {
            deleteExpenseRepository.deleteExpense(EXPENSE_ID);
        } catch (RuntimeException e) {
            assertEquals("Unexpected error", e.getMessage());
        }
    }
}
