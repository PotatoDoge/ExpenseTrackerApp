package com.expensetrackerapp.infrastructure.outbound.adapters.Expense;

import com.expensetrackerapp.application.port.in.Expense.GetExpenses.GetExpensesFilters;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.repositories.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetExpensesRepositoryUnitTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private GetExpensesRepository getExpensesRepository;

    private GetExpensesFilters filters;
    private ExpenseEntity expenseEntity;

    @BeforeEach
    void setUp() {
        filters = GetExpensesFilters.builder()
                .expenseId(1L)
                .expenseName("Lunch")
                .description("Team lunch")
                .amount(BigDecimal.valueOf(50.00))
                .currency("USD")
                .expenseDate(LocalDate.of(2024, 4, 1))
                .requiresInvoice(true)
                .installments(1)
                .vendor("Vendor Inc.")
                .location("New York")
                .paidInFull(true)
                .build();

        expenseEntity = ExpenseEntity.builder()
                .id(1L)
                .name("Lunch")
                .description("Team lunch")
                .amount(BigDecimal.valueOf(50.00))
                .currency("USD")
                .expenseDate(filters.getExpenseDate())
                .requiresInvoice(true)
                .installments(1)
                .vendor("Vendor Inc.")
                .location("New York")
                .isPaidInFull(true)
                .build();
    }

    @Test
    void shouldFetchExpensesGivenValidFilters() {
        // Arrange
        List<ExpenseEntity> expectedExpenses = List.of(expenseEntity);
        when(expenseRepository.findAllExpensesByFilters(
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
                filters.getPaidInFull()
        )).thenReturn(expectedExpenses);

        // Act
        List<ExpenseEntity> result = getExpensesRepository.getExpenses(filters);

        // Assert
        assertEquals(expectedExpenses, result);
        assertEquals(1, result.size());
        assertEquals("Lunch", result.getFirst().getName());

        verify(expenseRepository, times(1)).findAllExpensesByFilters(
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
                filters.getPaidInFull()
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoExpensesMatchFilters() {
        // Arrange
        when(expenseRepository.findAllExpensesByFilters(
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
                filters.getPaidInFull()
        )).thenReturn(List.of());

        // Act
        List<ExpenseEntity> result = getExpensesRepository.getExpenses(filters);

        // Assert
        assertEquals(0, result.size());

        verify(expenseRepository, times(1)).findAllExpensesByFilters(
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
                filters.getPaidInFull()
        );
    }


}
