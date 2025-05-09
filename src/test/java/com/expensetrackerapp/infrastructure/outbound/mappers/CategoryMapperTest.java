package com.expensetrackerapp.infrastructure.outbound.mappers;

import com.expensetrackerapp.application.port.in.Category.SaveCategory.SaveCategoryRequest;
import com.expensetrackerapp.domain.model.Category;
import com.expensetrackerapp.dto.CategoryDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.shared.exceptions.MappingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private final CategoryMapper mapper = new CategoryMapper();

    @Test
    void fromRequestToPojo_shouldMapCorrectly() {
        SaveCategoryRequest request = SaveCategoryRequest.builder().name("Groceries").icon("🛒").build();

        Category result = mapper.fromRequestToPojo(request);

        assertEquals("Groceries", result.getName());
        assertEquals("🛒", result.getIcon());
    }

    @Test
    void fromPojoToEntity_shouldMapCorrectly() {
        Category category = Category.builder()
                .id(1L)
                .name("Travel")
                .icon("✈️")
                .build();

        CategoryEntity entity = mapper.fromPojoToEntity(category);

        assertEquals(1L, entity.getId());
        assertEquals("Travel", entity.getName());
        assertEquals("✈️", entity.getIcon());
    }

    @Test
    void fromEntityToDTO_shouldMapCorrectly() {
        CategoryEntity entity = CategoryEntity.builder()
                .id(5L)
                .name("Utilities")
                .icon("💡")
                .build();

        CategoryDTO dto = mapper.fromEntityToDTO(entity);

        assertEquals(5L, dto.getId());
        assertEquals("Utilities", dto.getName());
        assertEquals("💡", dto.getIcon());
    }

    @Test
    void fromRequestToPojo_shouldThrowMappingException_onNullRequest() {
        MappingException exception = assertThrows(MappingException.class, () -> {
            mapper.fromRequestToPojo(null);
        });
        assertTrue(exception.getMessage().contains("Error while mapping baseCategoryRequest"));
    }

    @Test
    void fromPojoToEntity_shouldThrowMappingException_onNullCategory() {
        MappingException exception = assertThrows(MappingException.class, () -> {
            mapper.fromPojoToEntity(null);
        });
        assertTrue(exception.getMessage().contains("Error while mapping entity to dto"));
    }

    @Test
    void fromEntityToDTO_shouldThrowMappingException_onNullEntity() {
        MappingException exception = assertThrows(MappingException.class, () -> {
            mapper.fromEntityToDTO(null);
        });
        assertTrue(exception.getMessage().contains("Error while mapping entity to dto"));
    }

    @Test
    void updateEntity_shouldDoNothing() {
        CategoryEntity existing = CategoryEntity.builder().id(5L).name("A").icon("🅰️").build();
        CategoryEntity update = CategoryEntity.builder().id(6L).name("B").icon("🅱️").build();

        mapper.updateEntity(existing, update); // no-op

        // No change expected
        assertEquals("B", existing.getName());
        assertEquals("🅱️", existing.getIcon());
    }
}
