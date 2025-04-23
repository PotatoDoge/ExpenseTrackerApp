package com.expensetrackerapp.infrastructure.outbound.adapters;

import com.expensetrackerapp.domain.enums.PaymentMethod;
import com.expensetrackerapp.domain.enums.RecurrenceType;
import com.expensetrackerapp.domain.model.*;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import com.expensetrackerapp.infrastructure.outbound.repositories.ExpenseRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveExpenseRepositoryUnitTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ExpenseMapper expenseMapper;

    @InjectMocks
    private SaveExpenseRepository saveExpenseRepository;

    private Expense expense;
    private ExpenseEntity expenseEntity;

    @BeforeEach
    public void setUp() {
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

        expenseEntity = ExpenseEntity.builder()
                .id(1L)
                .name("Lunch")
                .description("Description of expense")
                .amount(BigDecimal.valueOf(50.0))
                .currency(Currency.getInstance("USD"))
                .dateTime(expense.getDateTime())
                .paymentMethod(PaymentMethod.CARD)
                .requiresInvoice(true)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(false)
                .recurrenceType(RecurrenceType.MONTHLY)
                .vendor("Vendor A")
                .location("New York")
                .build();
    }

    @Test
    void shouldSaveExpenseAndReturnSavedEntity() {
        // Arrange
        when(expenseMapper.fromPojoToEntity(expense)).thenReturn(expenseEntity);
        when(expenseRepository.save(expenseEntity)).thenReturn(expenseEntity);

        // Act
        ExpenseEntity result = saveExpenseRepository.saveExpense(expense);

        // Assert
        assertNotNull(result);
        assertEquals(expenseEntity.getName(), result.getName());
        assertEquals(expenseEntity.getAmount(), result.getAmount());

        verify(expenseMapper).fromPojoToEntity(expense);
        verify(expenseRepository).save(expenseEntity);
    }
}
