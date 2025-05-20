package com.expensetrackerapp.application.service.Expense;

import com.expensetrackerapp.application.port.in.Expense.SaveExpense.SaveExpenseRequest;
import com.expensetrackerapp.application.port.out.Category.GetCategoryByIdOutboundPort;
import com.expensetrackerapp.application.port.out.Expense.SaveExpenseOutboundPort;
import com.expensetrackerapp.domain.enums.PaymentMethod;
import com.expensetrackerapp.domain.enums.RecurrenceType;
import com.expensetrackerapp.domain.model.*;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CategoryMapper;
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
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SaveExpenseServiceUnitTest {

    @Mock
    private SaveExpenseOutboundPort<ExpenseEntity> saveExpensePort;

    @Mock
    private GetCategoryByIdOutboundPort<CategoryEntity> getCategoryByIdOutboundPort;

    @Mock
    private ExpenseMapper expenseMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private SaveExpenseService saveExpenseService;

    private SaveExpenseRequest saveExpenseRequest;
    private Expense expense;
    private ExpenseEntity expenseEntity;
    private ExpenseDTO expenseDTO;
    private CategoryEntity categoryEntity;
    private Category category;

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
                .categoryId(1L)
                .tags(Map.of())
                .attachments(Set.of(new Attachment()))
                .build();

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
                .tags(Set.of(new Tag()))
                .attachments(Set.of(new Attachment()))
                .build();

        categoryEntity = CategoryEntity.builder().id(1L).name("Food").build();
        category = Category.builder().id(1L).name("Food").build();
        expense.setCategory(category);

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
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest)).thenReturn(expense);
        when(getCategoryByIdOutboundPort.getCategoryById(1L)).thenReturn(Optional.ofNullable(categoryEntity));
        when(categoryMapper.fromEntityToPOJO(categoryEntity)).thenReturn(category);
        when(saveExpensePort.saveExpense(expense)).thenReturn(expenseEntity);
        when(expenseMapper.fromEntityToDTO(expenseEntity)).thenReturn(expenseDTO);

        ExpenseDTO result = saveExpenseService.saveExpense(saveExpenseRequest);

        assertNotNull(result);
        assertEquals("Lunch", result.getName());
        assertEquals(BigDecimal.valueOf(50.0), result.getAmount());

        verify(expenseMapper).fromRequestToPojo(saveExpenseRequest);
        verify(getCategoryByIdOutboundPort).getCategoryById(1L);
        verify(categoryMapper).fromEntityToPOJO(categoryEntity);
        verify(saveExpensePort).saveExpense(expense);
        verify(expenseMapper).fromEntityToDTO(expenseEntity);
    }

    @Test
    void testSaveExpenseWithNullRequest() {
        NullRequestException exception = assertThrows(NullRequestException.class, () -> {
            saveExpenseService.saveExpense(null);
        });
        assertEquals("Request's body (SaveExpenseRequest) cannot be null", exception.getMessage());
    }

    @Test
    void testSaveExpenseThrowsMappingException() {
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest))
                .thenThrow(new MappingException("Mapping failed"));

        MappingException exception = assertThrows(MappingException.class, () -> {
            saveExpenseService.saveExpense(saveExpenseRequest);
        });

        assertEquals("Error while mapping expense: MappingException", exception.getMessage());
    }

    @Test
    void testSaveExpenseThrowsIllegalArgumentException() {
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest))
                .thenThrow(new IllegalArgumentException("Invalid argument"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            saveExpenseService.saveExpense(saveExpenseRequest);
        });

        assertEquals("Error while saving expense: Invalid argument", exception.getMessage());
    }

    @Test
    void testSaveExpenseThrowsPersistenceException() {
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest))
                .thenThrow(new PersistenceException("Persistence failed"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            saveExpenseService.saveExpense(saveExpenseRequest);
        });

        assertEquals("Error while saving expense: Persistence failed", exception.getMessage());
    }

    @Test
    void testSaveExpenseThrowsDataAccessException() {
        DataAccessException mockException = mock(DataAccessException.class);
        when(mockException.getMessage()).thenReturn("Data access error");

        when(expenseMapper.fromRequestToPojo(saveExpenseRequest)).thenThrow(mockException);

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            saveExpenseService.saveExpense(saveExpenseRequest);
        });

        assertEquals("Error while saving expense: Data access error", exception.getMessage());
    }

    @Test
    void testSaveExpenseThrowsNullPointerException() {
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest))
                .thenThrow(new NullPointerException("Null pointer"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            saveExpenseService.saveExpense(saveExpenseRequest);
        });

        assertEquals("Error while saving expense: Null pointer", exception.getMessage());
    }

    @Test
    void testSaveExpenseThrowsClassCastException() {
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest))
                .thenThrow(new ClassCastException("Class cast"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            saveExpenseService.saveExpense(saveExpenseRequest);
        });

        assertEquals("Error while saving expense: Class cast", exception.getMessage());
    }

    @Test
    void testSaveExpenseThrowsGenericException() {
        when(expenseMapper.fromRequestToPojo(saveExpenseRequest))
                .thenThrow(new RuntimeException("Unexpected error"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () -> {
            saveExpenseService.saveExpense(saveExpenseRequest);
        });

        assertEquals("Unhandled error while saving expenses.", exception.getMessage());
    }

}
