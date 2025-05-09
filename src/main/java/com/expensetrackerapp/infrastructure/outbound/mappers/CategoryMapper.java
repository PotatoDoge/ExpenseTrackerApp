package com.expensetrackerapp.infrastructure.outbound.mappers;

import com.expensetrackerapp.application.port.base.BaseCategoryRequest;
import com.expensetrackerapp.domain.model.Category;
import com.expensetrackerapp.dto.CategoryDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.shared.exceptions.MappingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CategoryMapper implements ExtendedMapper<Category, CategoryEntity, CategoryDTO, BaseCategoryRequest>{

    @Override
    public Category fromRequestToPojo(BaseCategoryRequest baseCategoryRequest) {
        try{
            return Category.builder()
                    .name(baseCategoryRequest.getName())
                    .icon(baseCategoryRequest.getIcon())
                    .build();
        }
        catch (Exception e) {
            log.error("Error occurred while mapping baseCategoryRequest to object: {}", e.getMessage(), e);
            throw new MappingException("Error while mapping baseCategoryRequest to object: " + e);
        }
    }

    @Override
    public void updateEntity(CategoryEntity existingEntity, CategoryEntity newEntity) {
        try{
            existingEntity.setName(newEntity.getName());
            existingEntity.setIcon(newEntity.getIcon());
        }
        catch (Exception e) {
            log.error("Error occurred while mapping new attributes to existing object: {}", e.getMessage(), e);
            throw new MappingException("Error while mapping new attributes to existing object: " + e);
        }
    }

    @Override
    public CategoryEntity fromPojoToEntity(Category category) {
        try{
            return CategoryEntity.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .icon(category.getIcon())
                    .build();
        }
        catch (Exception e) {
            log.error("Error occurred while mapping categoryEntity to dto: {}", e.getMessage(), e);
            throw new MappingException("Error while mapping entity to dto: " + e);
        }
    }

    @Override
    public CategoryDTO fromEntityToDTO(CategoryEntity categoryEntity) {
        try{
            return CategoryDTO.builder()
                    .id(categoryEntity.getId())
                    .name(categoryEntity.getName())
                    .icon(categoryEntity.getIcon())
                    .build();
        }
        catch (Exception e) {
            log.error("Error occurred while mapping categoryEntity to dto: {}", e.getMessage(), e);
            throw new MappingException("Error while mapping entity to dto: " + e);
        }
    }
}
