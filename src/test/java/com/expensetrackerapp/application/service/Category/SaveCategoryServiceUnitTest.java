package com.expensetrackerapp.application.service.Category;

import com.expensetrackerapp.application.port.in.Category.SaveCategory.SaveCategoryRequest;
import com.expensetrackerapp.application.port.out.Category.SaveCategoryOutboundPort;
import com.expensetrackerapp.domain.model.Category;
import com.expensetrackerapp.dto.CategoryDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CategoryMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import com.expensetrackerapp.shared.exceptions.NullRequestException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SaveCategoryServiceUnitTest {

    @Mock
    private SaveCategoryOutboundPort<CategoryEntity> saveCategoryPort;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private SaveCategoryService saveCategoryService;

    private SaveCategoryRequest request;
    private Category category;
    private CategoryEntity categoryEntity;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        request = SaveCategoryRequest.builder()
                .name("Utilities")
                .icon("💡")
                .build();

        category = Category.builder()
                .id(1L)
                .name("Utilities")
                .icon("💡")
                .build();

        categoryEntity = CategoryEntity.builder()
                .id(1L)
                .name("Utilities")
                .icon("💡")
                .build();

        categoryDTO = CategoryDTO.builder()
                .id(1L)
                .name("Utilities")
                .icon("💡")
                .build();
    }

    @Test
    void saveCategory_shouldReturnCategoryDTO_whenSuccessful() {
        when(categoryMapper.fromRequestToPojo(request)).thenReturn(category);
        when(saveCategoryPort.saveCategory(category)).thenReturn(categoryEntity);
        when(categoryMapper.fromEntityToDTO(categoryEntity)).thenReturn(categoryDTO);

        CategoryDTO result = saveCategoryService.saveCategory(request);

        assertNotNull(result);
        assertEquals("Utilities", result.getName());
        assertEquals(1L, result.getId());

        verify(categoryMapper).fromRequestToPojo(request);
        verify(saveCategoryPort).saveCategory(category);
        verify(categoryMapper).fromEntityToDTO(categoryEntity);
    }

    @Test
    void saveCategory_shouldThrowNullRequestException_whenRequestIsNull() {
        NullRequestException exception = assertThrows(NullRequestException.class,
                () -> saveCategoryService.saveCategory(null));

        assertEquals("Request's body (SaveCategoryRequest) cannot be null", exception.getMessage());
        verifyNoInteractions(categoryMapper, saveCategoryPort);
    }

    @Test
    void saveCategory_shouldThrowDatabaseInteractionException_whenPersistenceFails() {
        when(categoryMapper.fromRequestToPojo(request)).thenReturn(category);
        when(saveCategoryPort.saveCategory(category)).thenThrow(new PersistenceException("DB issue"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class,
                () -> saveCategoryService.saveCategory(request));

        assertTrue(exception.getMessage().contains("PersistenceException"));
        verify(categoryMapper).fromRequestToPojo(request);
        verify(saveCategoryPort).saveCategory(category);
        verify(categoryMapper, never()).fromEntityToDTO(any());
    }

    @Test
    void saveCategory_shouldThrowMappingException_whenMappingFails() {
        when(categoryMapper.fromRequestToPojo(request)).thenThrow(new MappingException("Mapping failed"));

        MappingException exception = assertThrows(MappingException.class,
                () -> saveCategoryService.saveCategory(request));

        assertTrue(exception.getMessage().contains("MappingException"));
        verify(categoryMapper).fromRequestToPojo(request);
        verifyNoMoreInteractions(categoryMapper, saveCategoryPort);
    }

    @Test
    void saveCategory_shouldThrowDatabaseInteractionException_whenUnexpectedExceptionOccurs() {
        when(categoryMapper.fromRequestToPojo(request)).thenThrow(new RuntimeException("Unexpected"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class,
                () -> saveCategoryService.saveCategory(request));

        assertEquals("Unhandled error while saving category.", exception.getMessage());
        verify(categoryMapper).fromRequestToPojo(request);
    }
}
