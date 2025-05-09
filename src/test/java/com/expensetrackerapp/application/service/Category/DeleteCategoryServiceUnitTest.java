package com.expensetrackerapp.application.service.Category;

import com.expensetrackerapp.application.port.out.Category.DeleteCategoryOutboundPort;
import com.expensetrackerapp.application.port.out.Category.GetCategoryByIdOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryServiceUnitTest {

    @Mock
    private DeleteCategoryOutboundPort deleteCategoryOutboundPort;

    @Mock
    private GetCategoryByIdOutboundPort<CategoryEntity> getCategoryByIdOutboundPort;

    @InjectMocks
    private DeleteCategoryService deleteCategoryService;

    @Test
    void deleteCategory_shouldSucceed_whenCategoryExists() {
        Long categoryId = 1L;
        CategoryEntity categoryEntity = new CategoryEntity();
        when(getCategoryByIdOutboundPort.getCategoryById(categoryId)).thenReturn(Optional.of(categoryEntity));

        assertDoesNotThrow(() -> deleteCategoryService.deleteCategory(categoryId));

        verify(deleteCategoryOutboundPort, times(1)).deleteCategory(categoryId);
    }

    @Test
    void deleteCategory_shouldThrowNotFound_whenCategoryDoesNotExist() {
        Long categoryId = 2L;
        when(getCategoryByIdOutboundPort.getCategoryById(categoryId)).thenReturn(Optional.empty());

        NotFoundInDatabase exception = assertThrows(NotFoundInDatabase.class, () ->
                deleteCategoryService.deleteCategory(categoryId));

        assertEquals("Category with id: 2 does not exist", exception.getMessage());
        verify(deleteCategoryOutboundPort, never()).deleteCategory(anyLong());
    }

    @Test
    void deleteCategory_shouldThrowDatabaseInteractionException_onPersistenceError() {
        Long categoryId = 3L;
        CategoryEntity categoryEntity = new CategoryEntity();
        when(getCategoryByIdOutboundPort.getCategoryById(categoryId)).thenReturn(Optional.of(categoryEntity));
        doThrow(new RuntimeException("Database failure")).when(deleteCategoryOutboundPort).deleteCategory(categoryId);

        assertThrows(DatabaseInteractionException.class, () ->
                deleteCategoryService.deleteCategory(categoryId));

        verify(deleteCategoryOutboundPort).deleteCategory(categoryId);
    }

    @Test
    void deleteCategory_shouldThrowDatabaseInteractionException_onUnexpectedException() {
        Long categoryId = 4L;
        CategoryEntity categoryEntity = new CategoryEntity();
        when(getCategoryByIdOutboundPort.getCategoryById(categoryId)).thenReturn(Optional.of(categoryEntity));

        doThrow(new NullPointerException("Unexpected exception"))
                .when(deleteCategoryOutboundPort).deleteCategory(categoryId);

        DatabaseInteractionException exception = assertThrows(DatabaseInteractionException.class, () ->
                deleteCategoryService.deleteCategory(categoryId));

        assertTrue(exception.getMessage().contains("Error occurred while deleting category with id:"));
        verify(deleteCategoryOutboundPort).deleteCategory(categoryId);
    }

}
