package com.expensetrackerapp.infrastructure.outbound.mappers;

import com.expensetrackerapp.application.port.in.SaveExpense.SaveExpenseRequest;
import com.expensetrackerapp.domain.model.Expense;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper implements ExtendedMapper<Expense, ExpenseEntity, ExpenseDTO, SaveExpenseRequest> {

    @Override
    public ExpenseEntity fromPojoToEntity(Expense e) {
        return ExpenseEntity.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .amount(e.getAmount())
                .currency(e.getCurrency())
                .dateTime(e.getDateTime())
                .paymentMethod(e.getPaymentMethod())
                .requiresInvoice(e.isRequiresInvoice())
                .isPaidInFull(e.isPaidInFull())
                .installments(e.getInstallments())
                .isRecurring(e.isRecurring())
                .recurrenceType(e.getRecurrenceType())
                .vendor(e.getVendor())
                .location(e.getLocation())
                .build();
    }

    @Override
    public ExpenseDTO fromEntityToDTO(ExpenseEntity e) {
        return ExpenseDTO.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .amount(e.getAmount())
                .currency(e.getCurrency())
                .dateTime(e.getDateTime())
                .requiresInvoice(e.isRequiresInvoice())
                .isPaidInFull(e.isPaidInFull())
                .installments(e.getInstallments())
                .isRecurring(e.isRecurring())
                .vendor(e.getVendor())
                .location(e.getLocation())
                .build();
    }


    @Override
    public Expense fromRequestToPojo(SaveExpenseRequest saveExpenseRequest) {
        return Expense.builder()
                .name(saveExpenseRequest.getName())
                .description(saveExpenseRequest.getDescription())
                .amount(saveExpenseRequest.getAmount())
                .currency(saveExpenseRequest.getCurrency())
                .dateTime(saveExpenseRequest.getDateTime())
                .paymentMethod(saveExpenseRequest.getPaymentMethod())
                .requiresInvoice(saveExpenseRequest.isRequiresInvoice())
                .isPaidInFull(saveExpenseRequest.isPaidInFull())
                .installments(saveExpenseRequest.getInstallments())
                .isRecurring(saveExpenseRequest.isRecurring())
                .recurrenceType(saveExpenseRequest.getRecurrenceType())
                .vendor(saveExpenseRequest.getVendor())
                .location(saveExpenseRequest.getLocation())
                .card(saveExpenseRequest.getCard())
                .category(saveExpenseRequest.getCategory())
                .tags(saveExpenseRequest.getTags())
                .attachments(saveExpenseRequest.getAttachments())
                .build();
    }
}
