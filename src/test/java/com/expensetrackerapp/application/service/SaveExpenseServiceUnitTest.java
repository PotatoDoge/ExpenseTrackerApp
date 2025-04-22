package com.expensetrackerapp.application.service;

import com.expensetrackerapp.application.port.in.SaveExpense.SaveExpenseRequest;
import com.expensetrackerapp.application.port.out.SaveExpenseOutboundPort;
import com.expensetrackerapp.domain.enums.PaymentMethod;
import com.expensetrackerapp.domain.enums.RecurrenceType;
import com.expensetrackerapp.domain.model.*;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import com.expensetrackerapp.shared.exceptions.NullRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SaveExpenseServiceUnitTest{

    @Mock
    private SaveExpenseOutboundPort<ExpenseEntity> saveExpensePort;

    @Mock
    private ExpenseMapper expenseMapper;

    @InjectMocks
    private SaveExpenseService saveExpenseService;

    private SaveExpenseRequest saveExpenseRequest;
    private Expense expense;
    private ExpenseEntity expenseEntity;
    private ExpenseDTO expenseDTO;

    @BeforeEach
    public void setUp() {
        saveExpenseRequest = SaveExpenseRequest.builder()
                .name("Lunch")
                .description("Description of expense")
                .amount(BigDecimal.valueOf(50.0))
                .currency(Currency.getInstance("USD"))
                .dateTime(LocalDateTime.now())
                .paymentMethod(PaymentMethod.CARD)
                .requiresInvoice(true)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(false)
                .recurrenceType(RecurrenceType.MONTHLY)
                .vendor("Vendor A")
                .location("New York")
                .card(new Card())
                .category(new Category())
                .tags(Set.of(new Tag()))
                .attachments(Set.of(new Attachment()))
                .build();

        // Mock the Expense entity returned from the repository
        expense = Expense.builder()
                .id(1L)
                .name("Lunch")
                .description("Description of expense")
                .amount(BigDecimal.valueOf(50.0))
                .currency(Currency.getInstance("USD"))
                .dateTime(LocalDateTime.now())
                .paymentMethod(PaymentMethod.CARD)
                .requiresInvoice(true)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(false)
                .recurrenceType(RecurrenceType.MONTHLY)
                .vendor("Vendor A")
                .location("New York")
                .card(new Card())
                .category(new Category())
                .tags(Set.of(new Tag()))
                .attachments(Set.of(new Attachment()))
                .build();

        // Mock the ExpenseEntity that will be returned by the saveExpensePort
        expenseEntity = ExpenseEntity.builder()
                .id(1L)
                .name("Lunch")
                .description("Description of expense")
                .amount(BigDecimal.valueOf(50.0))
                .currency(Currency.getInstance("USD"))
                .dateTime(LocalDateTime.now())
                .paymentMethod(PaymentMethod.CARD)
                .requiresInvoice(true)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(false)
                .recurrenceType(RecurrenceType.MONTHLY)
                .vendor("Vendor A")
                .location("New York")
                .build();

        // Mock the ExpenseDTO that will be returned after mapping
        expenseDTO = ExpenseDTO.builder()
                .id(1L)
                .name("Lunch")
                .description("Description of expense")
                .amount(BigDecimal.valueOf(50.0))
                .currency(Currency.getInstance("USD"))
                .dateTime(LocalDateTime.now())
                .requiresInvoice(true)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(false)
                .vendor("Vendor A")
                .location("New York")
                .build();
    }


    @Test
    void testSaveExpense() {
        // Arrange
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest)).thenReturn(expense);
        when(saveExpensePort.saveExpense(expense)).thenReturn(expenseEntity);
        when(expenseMapper.fromEntityToDTO(expenseEntity)).thenReturn(expenseDTO);

        // Act
        ExpenseDTO result = saveExpenseService.saveExpense(saveExpenseRequest);

        // Assert
        verify(expenseMapper).fromRequestToPojo(saveExpenseRequest);
        verify(saveExpensePort).saveExpense(expense);
        verify(expenseMapper).fromEntityToDTO(expenseEntity);

        assertNotNull(result);
        assertEquals("Lunch", result.getName());
        assertEquals(BigDecimal.valueOf(50.0), result.getAmount());
    }

    @Test
    void testSaveExpenseWithNullRequest() {
        // Act
        NullRequestException exception = assertThrows(NullRequestException.class, () -> {
            saveExpenseService.saveExpense(null);
        });

        // Assert
        assertNotNull(exception);
    }

}
