package com.expensetrackerapp.application.service.Category;

import com.expensetrackerapp.application.port.in.Category.GetCategories.GetCategoriesFilters;
import com.expensetrackerapp.application.port.out.Category.GetCategoriesOutboundPort;
import com.expensetrackerapp.dto.CategoryDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CategoryMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCategoriesServiceUnitTest {

    @Mock
    private GetCategoriesOutboundPort<CategoryEntity, GetCategoriesFilters> getCategoriesPort;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private GetCategoriesService getCategoriesService;

    private CategoryEntity categoryEntity;
    private CategoryDTO categoryDTO;
    private GetCategoriesFilters filters;

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

        categoryDTO = CategoryDTO.builder()
                .id(1L)
                .name("Food")
                .icon("food-icon")
                .build();
    }

    @Test
    void getCategories_shouldReturnMappedCategoryDTOs() {
        when(getCategoriesPort.getCategories(filters)).thenReturn(List.of(categoryEntity));
        when(categoryMapper.fromEntityToDTO(categoryEntity)).thenReturn(categoryDTO);

        List<CategoryDTO> result = getCategoriesService.getCategories(filters);

        assertEquals(1, result.size());
        assertEquals("Food", result.get(0).getName());
        verify(getCategoriesPort).getCategories(filters);
        verify(categoryMapper).fromEntityToDTO(categoryEntity);
    }

    @Test
    void getCategories_shouldThrowMappingException_whenMapperFails() {
        when(getCategoriesPort.getCategories(filters)).thenReturn(List.of(categoryEntity));
        when(categoryMapper.fromEntityToDTO(categoryEntity)).thenThrow(new MappingException("Mapping failed"));

        MappingException exception = assertThrows(MappingException.class, () ->
                getCategoriesService.getCategories(filters));

        assertTrue(exception.getMessage().contains("Error while mapping category"));
    }

    @Test
    void getCategories_shouldThrowDatabaseInteractionException_whenPersistenceExceptionOccurs() {
        when(getCategoriesPort.getCategories(filters)).thenThrow(new PersistenceException("DB failure"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () ->
                getCategoriesService.getCategories(filters));

        assertTrue(exception.getMessage().contains("PersistenceException"));
    }

    @Test
    void getCategories_shouldThrowDatabaseInteractionException_whenNullPointerExceptionOccurs() {
        when(getCategoriesPort.getCategories(filters)).thenThrow(new NullPointerException("null!"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () ->
                getCategoriesService.getCategories(filters));

        assertTrue(exception.getMessage().contains("NullPointerException"));
    }

    @Test
    void getCategories_shouldThrowDatabaseInteractionException_whenUnexpectedExceptionOccurs() {
        when(getCategoriesPort.getCategories(filters)).thenThrow(new RuntimeException("Unexpected"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () ->
                getCategoriesService.getCategories(filters));

        assertTrue(exception.getMessage().contains("Unhandled error"));
    }
}
