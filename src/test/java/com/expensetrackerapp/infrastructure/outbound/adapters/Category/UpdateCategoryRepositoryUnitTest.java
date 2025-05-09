package com.expensetrackerapp.infrastructure.outbound.adapters.Category;

import com.expensetrackerapp.domain.model.Category;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CategoryMapper;
import com.expensetrackerapp.infrastructure.outbound.repositories.CategoryRepository;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryRepositoryUnitTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private UpdateCategoryRepository updateCategoryRepository;

    private Long categoryId;
    private Category category;
    private CategoryEntity existingEntity;
    private CategoryEntity mappedEntity;

    @BeforeEach
    void setUp() {
        categoryId = 1L;
        category = new Category();
        existingEntity = new CategoryEntity();
        mappedEntity = new CategoryEntity();
    }

    @Test
    void updateCategory_shouldSucceed_whenCategoryExists() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingEntity));
        when(categoryMapper.fromPojoToEntity(category)).thenReturn(mappedEntity);

        doNothing().when(categoryMapper).updateEntity(existingEntity, mappedEntity);
        when(categoryRepository.save(existingEntity)).thenReturn(existingEntity);

        CategoryEntity result = updateCategoryRepository.updateCategory(category, categoryId);

        assertEquals(existingEntity, result);
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).fromPojoToEntity(category);
        verify(categoryMapper).updateEntity(existingEntity, mappedEntity);
        verify(categoryRepository).save(existingEntity);
    }

    @Test
    void updateCategory_shouldThrowNotFoundInDatabase_whenCategoryDoesNotExist() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        NotFoundInDatabase exception = assertThrows(NotFoundInDatabase.class, () ->
                updateCategoryRepository.updateCategory(category, categoryId));

        assertEquals("Category with id 1 not found in database", exception.getMessage());
        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }
}
