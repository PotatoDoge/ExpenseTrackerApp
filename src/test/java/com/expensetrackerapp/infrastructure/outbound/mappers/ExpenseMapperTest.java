package com.expensetrackerapp.infrastructure.outbound.mappers;

import com.expensetrackerapp.application.port.BaseExpenseRequest;
import com.expensetrackerapp.domain.enums.PaymentMethod;
import com.expensetrackerapp.domain.enums.RecurrenceType;
import com.expensetrackerapp.domain.model.Expense;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.shared.exceptions.MappingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseMapperTest {

    private ExpenseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ExpenseMapper();
    }

    private Expense createSampleExpense() {
        return Expense.builder()
                .id(1L)
                .name("Lunch")
                .description("Lunch with client")
                .amount(new BigDecimal("20.50"))
                .currency("USD")
                .expenseDate(LocalDate.of(2024, 5, 5))
                .paymentMethod(PaymentMethod.CARD)
                .requiresInvoice(true)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(false)
                .recurrenceType(RecurrenceType.NONE)
                .vendor("Some Vendor")
                .location("New York")
                .build();
    }

    private ExpenseEntity createSampleEntity() {
        return ExpenseEntity.builder()
                .id(1L)
                .name("Lunch")
                .description("Lunch with client")
                .amount(new BigDecimal("20.50"))
                .currency("USD")
                .expenseDate(LocalDate.of(2024, 5, 5))
                .paymentMethod(PaymentMethod.CARD)
                .requiresInvoice(true)
                .isPaidInFull(true)
                .installments(1)
                .isRecurring(false)
                .recurrenceType(RecurrenceType.NONE)
                .vendor("Some Vendor")
                .location("New York")
                .build();
    }

    private BaseExpenseRequest createSampleRequest() {
        return new BaseExpenseRequest() {{
            setName("Lunch");
            setDescription("Lunch with client");
            setAmount(new BigDecimal("20.50"));
            setCurrency("USD");
            setExpenseDate(LocalDate.of(2024, 5, 5));
            setPaymentMethod(PaymentMethod.CARD);
            setRequiresInvoice(true);
            setIsPaidInFull(true);
            setInstallments(1);
            setIsRecurring(false);
            setRecurrenceType(RecurrenceType.NONE);
            setVendor("Some Vendor");
            setLocation("New York");
        }};
    }

    @Test
    void testFromPojoToEntity() {
        Expense expense = createSampleExpense();
        ExpenseEntity entity = mapper.fromPojoToEntity(expense);

        assertEquals(expense.getName(), entity.getName());
        assertEquals(expense.getAmount(), entity.getAmount());
        assertEquals(expense.getExpenseDate(), entity.getExpenseDate());
        assertEquals(expense.getLocation(), entity.getLocation());
    }

    @Test
    void testFromEntityToDTO() {
        ExpenseEntity entity = createSampleEntity();
        ExpenseDTO dto = mapper.fromEntityToDTO(entity);

        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getAmount(), dto.getAmount());
        assertEquals(entity.getExpenseDate(), dto.getExpenseDate());
        assertEquals(entity.getLocation(), dto.getLocation());
    }

    @Test
    void testUpdateEntity() {
        ExpenseEntity existing = ExpenseEntity.builder()
                .name("Old")
                .build();

        ExpenseEntity newData = createSampleEntity();
        mapper.updateEntity(existing, newData);

        assertEquals("Lunch", existing.getName());
        assertEquals(new BigDecimal("20.50"), existing.getAmount());
    }

    @Test
    void testFromRequestToPojo() {
        BaseExpenseRequest request = createSampleRequest();
        Expense expense = mapper.fromRequestToPojo(request);

        assertEquals(request.getName(), expense.getName());
        assertEquals(request.getAmount(), expense.getAmount());
        assertEquals(request.getExpenseDate(), expense.getExpenseDate());
        assertEquals(request.getLocation(), expense.getLocation());
    }

    @Test
    void testFromPojoToEntity_ThrowsException() {
        Expense broken = null;
        assertThrows(MappingException.class, () -> mapper.fromPojoToEntity(broken));
    }

    @Test
    void testFromEntityToDTO_ThrowsException() {
        ExpenseEntity broken = null;
        assertThrows(MappingException.class, () -> mapper.fromEntityToDTO(broken));
    }

    @Test
    void testUpdateEntity_ThrowsException() {
        assertThrows(MappingException.class, () -> mapper.updateEntity(null, null));
    }

    @Test
    void testFromRequestToPojo_ThrowsException() {
        assertThrows(MappingException.class, () -> mapper.fromRequestToPojo(null));
    }
}
