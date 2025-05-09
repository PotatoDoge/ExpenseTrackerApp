package com.expensetrackerapp.application.service.Category;

import com.expensetrackerapp.application.port.in.Category.DeleteCategory.DeleteCategoryUseCase;
import com.expensetrackerapp.application.port.out.Category.DeleteCategoryOutboundPort;
import com.expensetrackerapp.application.port.out.Category.GetCategoryByIdOutboundPort;
import com.expensetrackerapp.application.port.out.Expense.GetExpenseByIdOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class DeleteCategoryService implements DeleteCategoryUseCase {

    private final DeleteCategoryOutboundPort deleteCategoryPort;
    private final GetCategoryByIdOutboundPort<CategoryEntity> getCategoryByIdOutboundPort;

    @Override
    public void deleteCategory(Long categoryId) {
        try {
            Optional<CategoryEntity> existingCategory = getCategoryByIdOutboundPort.getCategoryById(categoryId);

            if (existingCategory.isEmpty()) {
                log.warn("Category with id: {} does not exist", categoryId);
                throw new NotFoundInDatabase("Category with id: " + categoryId + " does not exist");
            }
            log.info("Deleting category with id: {}", categoryId);
            deleteCategoryPort.deleteCategory(categoryId);
        }
        catch (NotFoundInDatabase e) {
            throw e;
        }
        catch (IllegalArgumentException | PersistenceException | DataAccessException |
               NullPointerException | ClassCastException e) {
            log.error("Error occurred while deleting category with id: {}", categoryId, e);
            throw new DatabaseInteractionException("Error occurred while deleting category with id: " + categoryId);
        }
        catch (Exception e) {
            log.error("Unexpected error occurred while deleting category with id: {}", categoryId, e);
            throw new DatabaseInteractionException("Unexpected error occurred while deleting category with id: " + categoryId);
        }
    }
}
