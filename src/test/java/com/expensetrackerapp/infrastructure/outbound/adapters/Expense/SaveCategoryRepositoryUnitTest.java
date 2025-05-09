package com.expensetrackerapp.infrastructure.outbound.adapters.Expense;

import com.expensetrackerapp.domain.model.Category;
import com.expensetrackerapp.infrastructure.outbound.adapters.Category.SaveCategoryRepository;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CategoryMapper;
import com.expensetrackerapp.infrastructure.outbound.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SaveCategoryRepositoryUnitTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private SaveCategoryRepository saveCategoryRepository;

    private Category domainCategory;
    private CategoryEntity entityToSave;
    private CategoryEntity savedEntity;

    @BeforeEach
    void setUp() {
        domainCategory = Category.builder()
                .id(1L)
                .name("Food")
                .icon("🍕")
                .build();

        entityToSave = CategoryEntity.builder()
                .id(1L)
                .name("Food")
                .icon("🍕")
                .build();

        savedEntity = CategoryEntity.builder()
                .id(1L)
                .name("Food")
                .icon("🍕")
                .build();
    }

    @Test
    void saveCategory_shouldMapAndSaveCategorySuccessfully() {
        when(categoryMapper.fromPojoToEntity(domainCategory)).thenReturn(entityToSave);
        when(categoryRepository.save(entityToSave)).thenReturn(savedEntity);

        CategoryEntity result = saveCategoryRepository.saveCategory(domainCategory);

        assertNotNull(result);
        assertEquals("Food", result.getName());
        assertEquals(1L, result.getId());
        verify(categoryMapper).fromPojoToEntity(domainCategory);
        verify(categoryRepository).save(entityToSave);
    }
}
