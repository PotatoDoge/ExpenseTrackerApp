package com.expensetrackerapp.application.service.Category;

import com.expensetrackerapp.application.port.in.Category.SaveCategory.SaveCategoryRequest;
import com.expensetrackerapp.application.port.in.Category.SaveCategory.SaveCategoryUseCase;
import com.expensetrackerapp.application.port.out.Category.SaveCategoryOutboundPort;
import com.expensetrackerapp.domain.model.Category;
import com.expensetrackerapp.dto.CategoryDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CategoryMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
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
public class SaveCategoryService implements SaveCategoryUseCase<CategoryDTO> {

    private final SaveCategoryOutboundPort<CategoryEntity> saveCategoryPort;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDTO saveCategory(SaveCategoryRequest saveCategoryRequest) {
        if (Objects.isNull(saveCategoryRequest)) {
            log.error("Request's body (SaveCategoryRequest) cannot be null");
            throw new NullRequestException("Request's body (SaveCategoryRequest) cannot be null");
        }

        Category category;
        try{
            category = categoryMapper.fromRequestToPojo(saveCategoryRequest);
            log.info("Saving category: {}", category);
            CategoryEntity categoryEntity = saveCategoryPort.saveCategory(category);
            return categoryMapper.fromEntityToDTO(categoryEntity);
        }
        catch (IllegalArgumentException | PersistenceException | DataAccessException |
               NullPointerException | ClassCastException e) {
            log.error("Error occurred while saving category: {}", e.getClass().getSimpleName(), e);
            throw new DatabaseInteractionException("Error while saving category: " + e.getClass().getSimpleName());
        }
        catch (MappingException e) {
            throw new MappingException("Error while mapping category: " + e.getClass().getSimpleName());
        }
        catch (Exception e) {
            log.error("Unexpected error occurred while saving category", e);
            throw new DatabaseInteractionException("Unhandled error while saving category.");
        }
    }
}
