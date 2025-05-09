package com.expensetrackerapp.infrastructure.outbound.adapters.Category;

import com.expensetrackerapp.application.port.out.Category.UpdateCategoryOutboundPort;
import com.expensetrackerapp.domain.model.Category;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CategoryMapper;
import com.expensetrackerapp.infrastructure.outbound.repositories.CategoryRepository;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
@Log4j2
public class UpdateCategoryRepository implements UpdateCategoryOutboundPort<CategoryEntity> {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryEntity updateCategory(Category category, Long categoryId) {
        Optional<CategoryEntity> categoryToBeUpdated = categoryRepository.findById(categoryId);
        if(categoryToBeUpdated.isEmpty()) {
            log.error("Category with id {} not found", categoryId);
            throw new NotFoundInDatabase("Category with id " + categoryId + " not found in database");
        }

        CategoryEntity existingCategory = categoryToBeUpdated.get();
        categoryMapper.updateEntity(existingCategory, categoryMapper.fromPojoToEntity(category));
        categoryRepository.save(existingCategory);

        log.info("Updated category: {}", existingCategory);

        return existingCategory;
    }
}
