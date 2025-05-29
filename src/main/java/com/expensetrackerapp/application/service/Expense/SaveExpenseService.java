package com.expensetrackerapp.application.service.Expense;

import com.expensetrackerapp.application.port.in.Expense.SaveExpense.SaveExpenseRequest;
import com.expensetrackerapp.application.port.in.Expense.SaveExpense.SaveExpenseUseCase;
import com.expensetrackerapp.application.port.out.Card.GetCardByIdOutboundPort;
import com.expensetrackerapp.application.port.out.Category.GetCategoryByIdOutboundPort;
import com.expensetrackerapp.application.port.out.Expense.SaveExpenseOutboundPort;
import com.expensetrackerapp.application.port.out.Tag.GetTagByNameOutboundPort;
import com.expensetrackerapp.application.port.out.Tag.SaveTagOutboundPort;
import com.expensetrackerapp.domain.model.Expense;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.entities.TagEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CardMapper;
import com.expensetrackerapp.infrastructure.outbound.mappers.CategoryMapper;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import com.expensetrackerapp.infrastructure.outbound.mappers.TagMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import com.expensetrackerapp.shared.exceptions.NullRequestException;
import jakarta.persistence.PersistenceException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Log4j2
public class SaveExpenseService extends BaseExpenseService implements SaveExpenseUseCase<ExpenseDTO> {

    private final SaveExpenseOutboundPort<ExpenseEntity> saveExpensePort;
    private final ExpenseMapper expenseMapper;

    public SaveExpenseService(
            SaveExpenseOutboundPort<ExpenseEntity> saveExpensePort,
            ExpenseMapper expenseMapper,
            GetCategoryByIdOutboundPort<CategoryEntity> getCategoryByIdRepository,
            GetTagByNameOutboundPort<TagEntity> getTagByNameRepository,
            GetCardByIdOutboundPort<CardEntity> getCardByIdRepository,
            SaveTagOutboundPort<TagEntity> saveTagRepository,
            CategoryMapper categoryMapper,
            TagMapper tagMapper,
            CardMapper cardMapper
    ) {
        super(getCategoryByIdRepository, getTagByNameRepository, getCardByIdRepository, saveTagRepository, categoryMapper, tagMapper, cardMapper);
        this.saveExpensePort = saveExpensePort;
        this.expenseMapper = expenseMapper;
    }

    @Override
    public ExpenseDTO saveExpense(SaveExpenseRequest saveExpenseRequest) {

        if (Objects.isNull(saveExpenseRequest)) {
            log.error("Request's body (SaveExpenseRequest) cannot be null");
            throw new NullRequestException("Request's body (SaveExpenseRequest) cannot be null");
        }

        Expense expense;
        try{
            expense = expenseMapper.fromRequestToPojo(saveExpenseRequest);
            expense.setCategory(validateAndMapCategory(saveExpenseRequest.getCategoryId()));
            expense.setTags(validateAndMapTags(saveExpenseRequest.getTags()));
            expense.setCard(validateAndMapCard(saveExpenseRequest.getCardId()));
            log.info("Saving expense: {}", expense);
            ExpenseEntity expenseEntity = saveExpensePort.saveExpense(expense);
            return expenseMapper.fromEntityToDTO(expenseEntity);
        }
        catch (NotFoundInDatabase e){
            throw e;
        }
        catch (IllegalArgumentException | PersistenceException | DataAccessException |
               NullPointerException | ClassCastException e) {
            log.error("Error occurred while saving expense: {}", e.getClass().getSimpleName(), e);
            throw new DatabaseInteractionException("Error while saving expense: " + e.getMessage());
        }
        catch (MappingException e) {
            throw new MappingException("Error while mapping expense: " + e.getClass().getSimpleName());
        }
        catch (Exception e) {
            log.error("Unexpected error occurred while saving expense", e);
            throw new DatabaseInteractionException("Unhandled error while saving expenses.");
        }
    }
}
