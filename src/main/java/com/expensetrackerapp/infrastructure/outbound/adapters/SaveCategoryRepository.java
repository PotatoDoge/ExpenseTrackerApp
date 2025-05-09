package com.expensetrackerapp.infrastructure.outbound.adapters;

import com.expensetrackerapp.application.port.out.SaveCategoryOutboundPort;
import com.expensetrackerapp.domain.model.Category;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CategoryMapper;
import com.expensetrackerapp.infrastructure.outbound.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class SaveCategoryRepository implements SaveCategoryOutboundPort<CategoryEntity> {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryEntity saveCategory(Category category) {
        CategoryEntity savedCategory = categoryRepository.save(categoryMapper.fromPojoToEntity(category));
        log.info("Saved category: {}", savedCategory);
        return savedCategory;
    }
}
