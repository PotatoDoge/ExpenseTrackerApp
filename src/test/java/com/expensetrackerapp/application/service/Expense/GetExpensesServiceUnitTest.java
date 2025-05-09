package com.expensetrackerapp.application.service.Expense;

import com.expensetrackerapp.application.port.in.Expense.GetExpenses.GetExpensesFilters;
import com.expensetrackerapp.application.port.out.Expense.GetExpensesOutboundPort;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetExpensesServiceUnitTest {

    @Mock
    private GetExpensesOutboundPort<ExpenseEntity, GetExpensesFilters> getExpensesPort;

    @Mock
    private ExpenseMapper expenseMapper;

    @InjectMocks
    private GetExpensesService getExpensesService;

    private ExpenseEntity expenseEntity;
    private ExpenseDTO expenseDTO;
    private GetExpensesFilters filters;

    @BeforeEach
    void setUp() {
        filters = new GetExpensesFilters();
        expenseEntity = ExpenseEntity.builder()
                .id(1L)
                .name("Groceries")
                .amount(BigDecimal.valueOf(100.0))
                .currency("USD")
                .expenseDate(LocalDate.now())
                .build();

        expenseDTO = ExpenseDTO.builder()
                .id(1L)
                .name("Groceries")
                .amount(BigDecimal.valueOf(100.0))
                .currency("USD")
                .expenseDate(expenseEntity.getExpenseDate())
                .build();
    }

    @Test
    void shouldReturnMappedExpensesListSuccessfully() {
        // Arrange
        when(getExpensesPort.getExpenses(filters)).thenReturn(List.of(expenseEntity));
        when(expenseMapper.fromEntityToDTO(expenseEntity)).thenReturn(expenseDTO);

        // Act
        List<ExpenseDTO> result = getExpensesService.getExpenses(filters);

        // Assert
        assertEquals(1, result.size());
        assertEquals(expenseDTO.getName(), result.get(0).getName());
        verify(getExpensesPort).getExpenses(filters);
        verify(expenseMapper).fromEntityToDTO(expenseEntity);
    }

    @Test
    void shouldThrowDatabaseInteractionExceptionWhenRepositoryFails() {
        // Arrange
        when(getExpensesPort.getExpenses(filters)).thenThrow(new PersistenceException("DB failure"));

        // Act & Assert
        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class,
                () -> getExpensesService.getExpenses(filters));

        assertTrue(exception.getMessage().contains("PersistenceException"));
        verify(getExpensesPort).getExpenses(filters);
    }

    @Test
    void shouldThrowMappingExceptionWhenMapperFails() {
        // Arrange
        when(getExpensesPort.getExpenses(filters)).thenReturn(List.of(expenseEntity));
        when(expenseMapper.fromEntityToDTO(expenseEntity)).thenThrow(new MappingException("Mapping failed"));

        // Act & Assert
        MappingException exception = assertThrows(MappingException.class,
                () -> getExpensesService.getExpenses(filters));

        assertTrue(exception.getClass().equals(MappingException.class));
        verify(expenseMapper).fromEntityToDTO(expenseEntity);
    }

    @Test
    void shouldThrowDatabaseInteractionExceptionForUnhandledException() {
        // Arrange
        when(getExpensesPort.getExpenses(filters)).thenThrow(new RuntimeException("Something went wrong"));

        // Act & Assert
        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class,
                () -> getExpensesService.getExpenses(filters));

        assertTrue(exception.getMessage().contains("Unhandled error"));
        verify(getExpensesPort).getExpenses(filters);
    }
}
