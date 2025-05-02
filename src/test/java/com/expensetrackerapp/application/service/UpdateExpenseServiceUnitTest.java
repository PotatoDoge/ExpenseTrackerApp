package com.expensetrackerapp.application.service;

import com.expensetrackerapp.application.port.in.UpdateExpense.UpdateExpenseRequest;
import com.expensetrackerapp.application.port.out.UpdateExpenseOutboundPort;
import com.expensetrackerapp.domain.enums.PaymentMethod;
import com.expensetrackerapp.domain.enums.RecurrenceType;
import com.expensetrackerapp.domain.model.*;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import com.expensetrackerapp.shared.exceptions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UpdateExpenseServiceUnitTest {

    @Mock
    private UpdateExpenseOutboundPort<ExpenseEntity> updateExpenseOutboundPort;

    @Mock
    private ExpenseMapper expenseMapper;

    @InjectMocks
    private UpdateExpenseService updateExpenseService;

    private UpdateExpenseRequest updateExpenseRequest;
    private Expense expense;
    private ExpenseEntity expenseEntity;
    private ExpenseDTO expenseDTO;

    private final Long expenseId = 1L;

    @BeforeEach
    void setUp() {
        updateExpenseRequest = UpdateExpenseRequest.builder()
                .name("Groceries")
                .description("Weekly groceries")
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .expenseDate(LocalDate.now())
                .paymentMethod(PaymentMethod.CASH)
                .requiresInvoice(false)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(true)
                .recurrenceType(RecurrenceType.WEEKLY)
                .vendor("Supermarket")
                .location("City Center")
                .card(new Card())
                .category(new Category())
                .tags(Set.of(new Tag()))
                .attachments(Set.of(new Attachment()))
                .build();

        expense = Expense.builder()
                .id(expenseId)
                .name("Groceries")
                .description("Weekly groceries")
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .expenseDate(LocalDate.now())
                .paymentMethod(PaymentMethod.CASH)
                .requiresInvoice(false)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(true)
                .recurrenceType(RecurrenceType.WEEKLY)
                .vendor("Supermarket")
                .location("City Center")
                .card(new Card())
                .category(new Category())
                .tags(Set.of(new Tag()))
                .attachments(Set.of(new Attachment()))
                .build();

        expenseEntity = ExpenseEntity.builder()
                .id(expenseId)
                .name("Groceries")
                .description("Weekly groceries")
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .expenseDate(LocalDate.now())
                .paymentMethod(PaymentMethod.CASH)
                .requiresInvoice(false)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(true)
                .recurrenceType(RecurrenceType.WEEKLY)
                .vendor("Supermarket")
                .location("City Center")
                .build();

        expenseDTO = ExpenseDTO.builder()
                .id(expenseId)
                .name("Groceries")
                .description("Weekly groceries")
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .expenseDate(LocalDate.now())
                .requiresInvoice(false)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(true)
                .vendor("Supermarket")
                .location("City Center")
                .build();
    }

    @Test
    void testUpdateExpenseSuccessfully() {
        when(expenseMapper.fromRequestToPojo(updateExpenseRequest)).thenReturn(expense);
        when(updateExpenseOutboundPort.updateExpense(expense, expenseId)).thenReturn(expenseEntity);
        when(expenseMapper.fromEntityToDTO(expenseEntity)).thenReturn(expenseDTO);

        ExpenseDTO result = updateExpenseService.updateExpense(updateExpenseRequest, expenseId);

        verify(expenseMapper).fromRequestToPojo(updateExpenseRequest);
        verify(updateExpenseOutboundPort).updateExpense(expense, expenseId);
        verify(expenseMapper).fromEntityToDTO(expenseEntity);

        assertNotNull(result);
        assertEquals("Groceries", result.getName());
        assertEquals(BigDecimal.valueOf(100), result.getAmount());
    }

    @Test
    void testUpdateExpenseWithNullRequest() {
        NullRequestException exception = assertThrows(NullRequestException.class, () -> {
            updateExpenseService.updateExpense(null, expenseId);
        });
        assertEquals("Request's body (UpdateExpenseRequest) cannot be null", exception.getMessage());
    }

    @Test
    void testUpdateExpenseThrowsNotFoundInDatabase() {
        when(expenseMapper.fromRequestToPojo(updateExpenseRequest)).thenReturn(expense);
        when(updateExpenseOutboundPort.updateExpense(expense, expenseId))
                .thenThrow(new NotFoundInDatabase("Expense not found"));

        NotFoundInDatabase exception = assertThrows(NotFoundInDatabase.class, () -> {
            updateExpenseService.updateExpense(updateExpenseRequest, expenseId);
        });
        assertEquals("Expense not found", exception.getMessage());
    }

    @Test
    void testUpdateExpenseThrowsMappingException() {
        when(expenseMapper.fromRequestToPojo(updateExpenseRequest))
                .thenThrow(new MappingException("Mapping error"));

        MappingException exception = assertThrows(MappingException.class, () -> {
            updateExpenseService.updateExpense(updateExpenseRequest, expenseId);
        });
        assertEquals("Error while mapping expense: MappingException", exception.getMessage());
    }

    @Test
    void testUpdateExpenseThrowsDatabaseInteractionException() {
        when(expenseMapper.fromRequestToPojo(updateExpenseRequest)).thenReturn(expense);
        when(updateExpenseOutboundPort.updateExpense(expense, expenseId))
                .thenThrow(new NullPointerException("Simulated DB failure"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            updateExpenseService.updateExpense(updateExpenseRequest, expenseId);
        });

        assertTrue(exception.getMessage().contains("Error while updating expense"));
    }

    @Test
    void testUpdateExpenseWithUnexpectedException() {
        when(expenseMapper.fromRequestToPojo(updateExpenseRequest))
                .thenThrow(new RuntimeException("Unexpected error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            updateExpenseService.updateExpense(updateExpenseRequest, 1L);
        });
        assertEquals("Unhandled error while updating expense.", exception.getMessage());
    }


}
