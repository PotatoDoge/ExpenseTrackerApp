package com.expensetrackerapp.infrastructure.outbound.adapters.Expense;

import com.expensetrackerapp.domain.enums.PaymentMethod;
import com.expensetrackerapp.domain.enums.RecurrenceType;
import com.expensetrackerapp.domain.model.*;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import com.expensetrackerapp.infrastructure.outbound.repositories.ExpenseRepository;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateExpenseRepositoryUnitTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ExpenseMapper expenseMapper;

    @InjectMocks
    private UpdateExpenseRepository updateExpenseRepository;

    private Expense expense;
    private ExpenseEntity existingEntity;
    private ExpenseEntity updatedEntity;

    @BeforeEach
    void setUp() {
        expense = Expense.builder()
                .id(1L)
                .name("Dinner")
                .description("Updated description")
                .amount(BigDecimal.valueOf(100.0))
                .currency("USD")
                .expenseDate(LocalDate.now())
                .paymentMethod(PaymentMethod.CARD)
                .requiresInvoice(false)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(false)
                .recurrenceType(RecurrenceType.NONE)
                .vendor("Updated Vendor")
                .location("Los Angeles")
                .card(new Card())
                .category(new Category())
                .tags(Set.of(new Tag()))
                .attachments(Set.of(new Attachment()))
                .build();

        existingEntity = ExpenseEntity.builder()
                .id(1L)
                .name("Old Name")
                .description("Old description")
                .amount(BigDecimal.valueOf(50.0))
                .currency("USD")
                .expenseDate(LocalDate.now().minusDays(1))
                .paymentMethod(PaymentMethod.CASH)
                .requiresInvoice(true)
                .isPaidInFull(false)
                .installments(2)
                .isRecurring(true)
                .recurrenceType(RecurrenceType.MONTHLY)
                .vendor("Old Vendor")
                .location("New York")
                .build();

        updatedEntity = ExpenseEntity.builder()
                .id(1L)
                .name("Dinner")
                .description("Updated description")
                .amount(BigDecimal.valueOf(100.0))
                .currency("USD")
                .expenseDate(LocalDate.now())
                .paymentMethod(PaymentMethod.CARD)
                .requiresInvoice(false)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(false)
                .recurrenceType(RecurrenceType.NONE)
                .vendor("Updated Vendor")
                .location("Los Angeles")
                .build();
    }

    @Test
    void shouldUpdateExpenseWhenExists() {
        // Arrange
        Long expenseId = 1L;

        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(existingEntity));
        when(expenseMapper.fromPojoToEntity(expense)).thenReturn(updatedEntity);
        when(expenseRepository.save(existingEntity)).thenReturn(existingEntity);

        // Simulate side effect of updateEntity (since it's void)
        doAnswer(invocation -> {
            ExpenseEntity target = invocation.getArgument(0); // existingEntity
            ExpenseEntity source = invocation.getArgument(1); // updatedEntity

            target.setName(source.getName());
            target.setDescription(source.getDescription());
            target.setAmount(source.getAmount());
            target.setCurrency(source.getCurrency());
            target.setExpenseDate(source.getExpenseDate());
            target.setPaymentMethod(source.getPaymentMethod());
            target.setRequiresInvoice(source.getRequiresInvoice());
            target.setIsPaidInFull(source.getIsPaidInFull());
            target.setInstallments(source.getInstallments());
            target.setIsRecurring(source.getIsRecurring());
            target.setRecurrenceType(source.getRecurrenceType());
            target.setVendor(source.getVendor());
            target.setLocation(source.getLocation());

            return null;
        }).when(expenseMapper).updateEntity(eq(existingEntity), eq(updatedEntity));

        // Act
        ExpenseEntity result = updateExpenseRepository.updateExpense(expense, expenseId);

        // Assert
        assertNotNull(result);
        assertEquals("Dinner", result.getName());
        verify(expenseRepository).findById(expenseId);
        verify(expenseMapper).fromPojoToEntity(expense);
        verify(expenseMapper).updateEntity(existingEntity, updatedEntity);
        verify(expenseRepository).save(existingEntity);
    }


    @Test
    void shouldThrowExceptionWhenExpenseNotFound() {
        // Arrange
        when(expenseRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundInDatabase exception = assertThrows(
                NotFoundInDatabase.class,
                () -> updateExpenseRepository.updateExpense(expense, 99L)
        );

        assertEquals("Expense with id " + expense.getId() + " not found in database", exception.getMessage());
        verify(expenseRepository).findById(99L);
        verify(expenseRepository, never()).save(any());
    }
}
