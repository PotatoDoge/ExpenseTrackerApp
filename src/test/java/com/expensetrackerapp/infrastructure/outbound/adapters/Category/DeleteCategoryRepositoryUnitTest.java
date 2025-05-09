package com.expensetrackerapp.infrastructure.outbound.adapters.Category;

import com.expensetrackerapp.infrastructure.outbound.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryRepositoryUnitTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private DeleteCategoryRepository deleteCategoryRepository;

    @Test
    void deleteCategory_shouldCallDeleteById() {
        // Given
        Long categoryId = 1L;

        // When
        deleteCategoryRepository.deleteCategory(categoryId);

        // Then
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}
