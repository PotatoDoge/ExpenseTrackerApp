package com.expensetrackerapp.application.service.Expense;

import com.expensetrackerapp.application.port.in.Expense.SaveExpense.SaveExpenseRequest;
import com.expensetrackerapp.application.port.out.Expense.SaveExpenseOutboundPort;
import com.expensetrackerapp.domain.enums.PaymentMethod;
import com.expensetrackerapp.domain.enums.RecurrenceType;
import com.expensetrackerapp.domain.model.*;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import com.expensetrackerapp.shared.exceptions.NullRequestException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.time.LocalDate;
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
                .currency("USD")
                .expenseDate(LocalDate.now())
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
                .currency("USD")
                .expenseDate(LocalDate.now())
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
                .currency("USD")
                .expenseDate(LocalDate.now())
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
                .currency("USD")
                .expenseDate(LocalDate.now())
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

    @Test
    void testSaveExpenseThrowsMappingException() {
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest)).thenThrow(new MappingException("Mapping failed"));

        MappingException exception = assertThrows(MappingException.class, () -> {
            saveExpenseService.saveExpense(saveExpenseRequest);
        });

        assertEquals("Error while mapping expense: MappingException", exception.getMessage());
    }

    @Test
    void testSaveExpenseThrowsIllegalArgumentException() {
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest)).thenThrow(new IllegalArgumentException("Invalid argument"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            saveExpenseService.saveExpense(saveExpenseRequest);
        });

        assertTrue(exception.getMessage().contains("IllegalArgumentException"));
    }

    @Test
    void testSaveExpenseThrowsPersistenceException() {
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest)).thenThrow(new PersistenceException("Persistence failed"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            saveExpenseService.saveExpense(saveExpenseRequest);
        });

        assertTrue(exception.getMessage().contains("PersistenceException"));
    }

    @Test
    void testSaveExpenseThrowsDataAccessException() {
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest)).thenThrow(mock(DataAccessException.class));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            saveExpenseService.saveExpense(saveExpenseRequest);
        });

        assertTrue(exception.getMessage().contains("DataAccessException"));
    }

    @Test
    void testSaveExpenseThrowsNullPointerException() {
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest)).thenThrow(new NullPointerException("Null pointer"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            saveExpenseService.saveExpense(saveExpenseRequest);
        });

        assertTrue(exception.getMessage().contains("NullPointerException"));
    }

    @Test
    void testSaveExpenseThrowsClassCastException() {
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest)).thenThrow(new ClassCastException("Class cast"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            saveExpenseService.saveExpense(saveExpenseRequest);
        });

        assertTrue(exception.getMessage().contains("ClassCastException"));
    }

    @Test
    void testSaveExpenseThrowsGenericException() {
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest)).thenThrow(new RuntimeException("Unexpected error"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            saveExpenseService.saveExpense(saveExpenseRequest);
        });

        assertEquals("Unhandled error while saving expensea.", exception.getMessage());
    }

}
