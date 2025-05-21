package com.expensetrackerapp.infrastructure.outbound.mappers;

import com.expensetrackerapp.application.port.base.BaseExpenseRequest;
import com.expensetrackerapp.domain.model.Expense;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.shared.exceptions.MappingException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Log4j2
@Component
@AllArgsConstructor
public class ExpenseMapper implements ExtendedMapper<Expense, ExpenseEntity, ExpenseDTO, BaseExpenseRequest> {

    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;

    @Override
    public ExpenseEntity fromPojoToEntity(Expense e) {
        try{
            return ExpenseEntity.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .description(e.getDescription())
                    .amount(e.getAmount())
                    .currency(e.getCurrency())
                    .expenseDate(e.getExpenseDate())
                    .paymentMethod(e.getPaymentMethod())
                    .requiresInvoice(e.getRequiresInvoice())
                    .isPaidInFull(e.getIsPaidInFull())
                    .installments(e.getInstallments())
                    .isRecurring(e.getIsRecurring())
                    .recurrenceType(e.getRecurrenceType())
                    .vendor(e.getVendor())
                    .location(e.getLocation())
                    .tags(e.getTags() != null
                            ? e.getTags().stream()
                            .map(tagMapper::fromPojoToEntity)
                            .collect(Collectors.toList())
                            : null)
                    .category(e.getCategory() != null ? categoryMapper.fromPojoToEntity(e.getCategory()) : null)
                    .build();
        }
        catch (Exception ex) {
            log.error("Error occurred while mapping object to entity: {}", ex.getMessage(), ex);
            throw new MappingException("Error while mapping object to entity: " + ex);
        }
    }

    @Override
    public ExpenseDTO fromEntityToDTO(ExpenseEntity e) {
        try{
            return ExpenseDTO.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .description(e.getDescription())
                    .amount(e.getAmount())
                    .currency(e.getCurrency())
                    .expenseDate(e.getExpenseDate())
                    .requiresInvoice(e.getRequiresInvoice())
                    .isPaidInFull(e.getIsPaidInFull())
                    .installments(e.getInstallments())
                    .isRecurring(e.getIsRecurring())
                    .vendor(e.getVendor())
                    .location(e.getLocation())
                    .tags(e.getTags() != null && !e.getTags().isEmpty()
                            ? e.getTags().stream()
                            .map(tagMapper::fromEntityToDTO)
                            .collect(Collectors.toList())
                            : null)
                    .category(e.getCategory() != null ? categoryMapper.fromEntityToDTO(e.getCategory()) : null)
                    .build();
        }
        catch (Exception ex) {
            log.error("Error occurred while mapping entity to dto: {}", ex.getMessage(), ex);
            throw new MappingException("Error while mapping entity to dto: " + ex);
        }
    }

    @Override
    public void updateEntity(ExpenseEntity existing, ExpenseEntity newData) {
        try{
            existing.setName(newData.getName());
            existing.setDescription(newData.getDescription());
            existing.setAmount(newData.getAmount());
            existing.setCurrency(newData.getCurrency());
            existing.setExpenseDate(newData.getExpenseDate());
            existing.setPaymentMethod(newData.getPaymentMethod());
            existing.setRequiresInvoice(newData.getRequiresInvoice());
            existing.setIsPaidInFull(newData.getIsPaidInFull());
            existing.setInstallments(newData.getInstallments());
            existing.setIsRecurring(newData.getIsRecurring());
            existing.setRecurrenceType(newData.getRecurrenceType());
            existing.setVendor(newData.getVendor());
            existing.setLocation(newData.getLocation());
            existing.setCategory(newData.getCategory());
            existing.getTags().clear();
            existing.getTags().addAll(newData.getTags());
        }
        catch (Exception ex) {
            log.error("Error occurred while updating (mapping) entity values: {}", ex.getMessage(), ex);
            throw new MappingException("Error occurred while updating (mapping) entity values: " + ex);
        }
    }

    @Override
    public Expense fromEntityToPOJO(ExpenseEntity expenseEntity) {
        return null;
    }

    @Override
    public Expense fromRequestToPojo(BaseExpenseRequest saveExpenseRequest) {
        try{
            return Expense.builder()
                    .name(saveExpenseRequest.getName())
                    .description(saveExpenseRequest.getDescription())
                    .amount(saveExpenseRequest.getAmount())
                    .currency(saveExpenseRequest.getCurrency())
                    .expenseDate(saveExpenseRequest.getExpenseDate())
                    .paymentMethod(saveExpenseRequest.getPaymentMethod())
                    .requiresInvoice(saveExpenseRequest.getRequiresInvoice())
                    .isPaidInFull(saveExpenseRequest.getIsPaidInFull())
                    .installments(saveExpenseRequest.getInstallments())
                    .isRecurring(saveExpenseRequest.getIsRecurring())
                    .recurrenceType(saveExpenseRequest.getRecurrenceType())
                    .vendor(saveExpenseRequest.getVendor())
                    .location(saveExpenseRequest.getLocation())
                    .card(saveExpenseRequest.getCard())
                    .attachments(saveExpenseRequest.getAttachments())
                    .build();
        }
        catch (Exception e) {
            log.error("Error occurred while mapping request to object: {}", e.getMessage(), e);
            throw new MappingException("Error while mapping request to object: " + e);
        }
    }
}
