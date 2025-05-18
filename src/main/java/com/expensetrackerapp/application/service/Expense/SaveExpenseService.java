package com.expensetrackerapp.application.service.Expense;

import com.expensetrackerapp.application.port.in.Expense.SaveExpense.SaveExpenseRequest;
import com.expensetrackerapp.application.port.in.Expense.SaveExpense.SaveExpenseUseCase;
import com.expensetrackerapp.application.port.out.Category.GetCategoryByIdOutboundPort;
import com.expensetrackerapp.application.port.out.Expense.SaveExpenseOutboundPort;
import com.expensetrackerapp.application.port.out.Tag.GetTagByNameOutboundPort;
import com.expensetrackerapp.application.port.out.Tag.SaveTagOutboundPort;
import com.expensetrackerapp.domain.model.Category;
import com.expensetrackerapp.domain.model.Expense;
import com.expensetrackerapp.domain.model.Tag;
import com.expensetrackerapp.dto.ExpenseDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.infrastructure.outbound.entities.TagEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CategoryMapper;
import com.expensetrackerapp.infrastructure.outbound.mappers.ExpenseMapper;
import com.expensetrackerapp.infrastructure.outbound.mappers.TagMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import com.expensetrackerapp.shared.exceptions.NullRequestException;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.expensetrackerapp.shared.constants.Colors.WHITE;

@Service
@AllArgsConstructor
@Log4j2
public class SaveExpenseService implements SaveExpenseUseCase<ExpenseDTO> {

    private final SaveExpenseOutboundPort<ExpenseEntity> saveExpensePort;
    private final GetCategoryByIdOutboundPort<CategoryEntity> getCategoryByIdRepository;
    private final GetTagByNameOutboundPort<TagEntity> getTagByNameRepository;
    private final SaveTagOutboundPort<TagEntity> saveTagRepository;
    private final ExpenseMapper expenseMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;

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

    private Category validateAndMapCategory(Long categoryId) {
        if (categoryId == null) return null;

        return getCategoryByIdRepository.getCategoryById(categoryId)
                .map(categoryMapper::fromEntityToPOJO)
                .orElseThrow(() -> new NotFoundInDatabase("Category not found with id: " + categoryId));
    }

    private Set<Tag> validateAndMapTags(Map<String, String> tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptySet();
        }

        return tags.entrySet().stream()
                .filter(entry -> entry.getKey() != null && !entry.getKey().isBlank()) // skip null/blank keys
                .map(entry -> {
                    String tagName = entry.getKey().toUpperCase();
                    String tagColor = (entry.getValue() == null || entry.getValue().isBlank())
                            ? WHITE
                            : entry.getValue().trim();
                    Optional<TagEntity> tagEntityOpt = getTagByNameRepository.getTagByName(tagName);
                    TagEntity tagEntity = tagEntityOpt.orElseGet(() ->
                            saveTagRepository.saveTag(tagMapper.fromMapEntryToTag(Map.entry(tagName, tagColor)))
                    );
                    return tagMapper.fromEntityToPOJO(tagEntity);
                })
                .collect(Collectors.toSet());
    }
}
