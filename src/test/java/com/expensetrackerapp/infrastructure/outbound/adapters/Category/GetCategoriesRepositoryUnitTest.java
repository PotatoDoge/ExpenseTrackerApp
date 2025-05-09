package com.expensetrackerapp.infrastructure.outbound.adapters.Category;

import com.expensetrackerapp.application.port.in.Category.GetCategories.GetCategoriesFilters;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCategoriesRepositoryUnitTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private GetCategoriesRepository getCategoriesRepository;

    private GetCategoriesFilters filters;
    private CategoryEntity categoryEntity;

    @BeforeEach
    void setUp() {
        filters = GetCategoriesFilters.builder()
                .categoryId(1L)
                .categoryName("Food")
                .build();

        categoryEntity = CategoryEntity.builder()
                .id(1L)
                .name("Food")
                .icon("food-icon")
                .build();
    }

    @Test
    void getCategories_shouldReturnMatchingCategories() {
        // Arrange
        List<CategoryEntity> mockResult = List.of(categoryEntity);
        when(categoryRepository.findAllCategoriesByFilters(1L, "Food")).thenReturn(mockResult);

        // Act
        List<CategoryEntity> result = getCategoriesRepository.getCategories(filters);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Food", result.getFirst().getName());
        verify(categoryRepository, times(1)).findAllCategoriesByFilters(1L, "Food");
    }

    @Test
    void getCategories_shouldReturnEmptyList() {
        // Arrange
        List<CategoryEntity> mockResult = List.of();
        when(categoryRepository.findAllCategoriesByFilters(1L, "Food")).thenReturn(mockResult);

        // Act
        List<CategoryEntity> result = getCategoriesRepository.getCategories(filters);

        // Assert
        assertEquals(0, result.size());
    }
}
