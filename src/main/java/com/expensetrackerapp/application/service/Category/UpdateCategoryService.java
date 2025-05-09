package com.expensetrackerapp.application.service.Category;

import com.expensetrackerapp.application.port.in.Category.UpdateCategory.UpdateCategoryRequest;
import com.expensetrackerapp.application.port.in.Category.UpdateCategory.UpdateCategoryUseCase;
import com.expensetrackerapp.application.port.out.Category.UpdateCategoryOutboundPort;
import com.expensetrackerapp.domain.model.Category;
import com.expensetrackerapp.dto.CategoryDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CategoryMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import com.expensetrackerapp.shared.exceptions.NullRequestException;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
@Log4j2
public class UpdateCategoryService implements UpdateCategoryUseCase<CategoryDTO> {

    private final UpdateCategoryOutboundPort<CategoryEntity> updateCategoryPort;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDTO updateCategory(UpdateCategoryRequest updateCategoryRequest, Long categoryId) {
        if (Objects.isNull(updateCategoryRequest)) {
            log.error("Request's body (UpdateCategoryRequest) cannot be null");
            throw new NullRequestException("Request's body (UpdateCategoryRequest) cannot be null");
        }
        Category category;
        try{
            category = categoryMapper.fromRequestToPojo(updateCategoryRequest);
            log.info("Updating category: {}", category);
            CategoryEntity categoryEntity = updateCategoryPort.updateCategory(category, categoryId);
            return categoryMapper.fromEntityToDTO(categoryEntity);
        }
        catch (NotFoundInDatabase e){
            throw e;
        }
        catch (IllegalArgumentException | PersistenceException | DataAccessException |
               NullPointerException | ClassCastException e) {
            log.error("Error occurred while updating category: {}", e.getClass().getSimpleName(), e);
            throw new DatabaseInteractionException("Error while updating category: " + e.getClass().getSimpleName());
        }
        catch (MappingException e) {
            throw new MappingException("Error while mapping category: " + e.getClass().getSimpleName());
        }
        catch (Exception e) {
            log.error("Unexpected error occurred while updating category", e);
            throw new DatabaseInteractionException("Unhandled error while updating category.");
        }
    }
}
