package com.expensetrackerapp.application.service.Category;

import com.expensetrackerapp.application.port.in.Category.UpdateCategory.UpdateCategoryRequest;
import com.expensetrackerapp.application.port.out.Category.UpdateCategoryOutboundPort;
import com.expensetrackerapp.domain.model.Category;
import com.expensetrackerapp.dto.CategoryDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CategoryMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import com.expensetrackerapp.shared.exceptions.NullRequestException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryServiceTest {

    @Mock
    private UpdateCategoryOutboundPort<CategoryEntity> updateCategoryPort;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private UpdateCategoryService updateCategoryService;

    private UpdateCategoryRequest updateCategoryRequest;
    private Category category;
    private final Long categoryId = 1L;

    @BeforeEach
    void setup() {
        updateCategoryRequest = UpdateCategoryRequest.builder()
                .name("Updated Name")
                .icon("updated-icon")
                .build();

        category = new Category(categoryId, "Updated Name", "updated-icon");
    }

    @Test
    void updateCategory_shouldReturnCategoryDTO_whenValidRequest() {
        CategoryEntity entity = new CategoryEntity();
        CategoryDTO dto = CategoryDTO.builder()
                .id(categoryId)
                .name("Updated Name")
                .icon("updated-icon")
                .build();

        when(categoryMapper.fromRequestToPojo(updateCategoryRequest)).thenReturn(category);
        when(updateCategoryPort.updateCategory(category, categoryId)).thenReturn(entity);
        when(categoryMapper.fromEntityToDTO(entity)).thenReturn(dto);

        CategoryDTO result = updateCategoryService.updateCategory(updateCategoryRequest, categoryId);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(updateCategoryPort).updateCategory(category, categoryId);
    }

    @Test
    void updateCategory_shouldThrowNullRequestException_whenRequestIsNull() {
        NullRequestException exception = assertThrows(NullRequestException.class,
                () -> updateCategoryService.updateCategory(null, categoryId));
        assertEquals("Request's body (UpdateCategoryRequest) cannot be null", exception.getMessage());
    }

    @Test
    void updateCategory_shouldThrowMappingException_whenMappingFails() {
        when(categoryMapper.fromRequestToPojo(updateCategoryRequest))
                .thenThrow(new MappingException("Mapping error"));

        MappingException exception = assertThrows(MappingException.class,
                () -> updateCategoryService.updateCategory(updateCategoryRequest, categoryId));

        assertEquals("Error while mapping category: MappingException", exception.getMessage());
    }

    @Test
    void updateCategory_shouldThrowDatabaseInteractionException_whenDatabaseFails() {
        when(categoryMapper.fromRequestToPojo(updateCategoryRequest)).thenReturn(category);
        when(updateCategoryPort.updateCategory(category, categoryId))
                .thenThrow(new PersistenceException("DB failure"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class,
                () -> updateCategoryService.updateCategory(updateCategoryRequest, categoryId));

        assertTrue(exception.getMessage().contains("PersistenceException"));
    }

    @Test
    void updateCategory_shouldRethrowNotFoundInDatabaseException() {
        when(categoryMapper.fromRequestToPojo(updateCategoryRequest)).thenReturn(category);
        when(updateCategoryPort.updateCategory(category, categoryId))
                .thenThrow(new NotFoundInDatabase("Not found"));

        NotFoundInDatabase exception = assertThrows(NotFoundInDatabase.class,
                () -> updateCategoryService.updateCategory(updateCategoryRequest, categoryId));

        assertEquals("Not found", exception.getMessage());
    }

    @Test
    void updateCategory_shouldHandleUnexpectedExceptions() {
        when(categoryMapper.fromRequestToPojo(updateCategoryRequest)).thenReturn(category);
        when(updateCategoryPort.updateCategory(category, categoryId))
                .thenThrow(new RuntimeException("Something went wrong"));

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class,
                () -> updateCategoryService.updateCategory(updateCategoryRequest, categoryId));

        assertTrue(exception.getMessage().contains("Unhandled error while updating category"));
    }
}
