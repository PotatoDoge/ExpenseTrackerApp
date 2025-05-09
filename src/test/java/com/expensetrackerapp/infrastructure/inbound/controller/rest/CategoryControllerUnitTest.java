package com.expensetrackerapp.infrastructure.inbound.controller.rest;

import com.expensetrackerapp.application.port.in.SaveCategory.SaveCategoryRequest;
import com.expensetrackerapp.application.port.in.SaveCategory.SaveCategoryUseCase;
import com.expensetrackerapp.dto.CategoryDTO;
import com.expensetrackerapp.shared.CustomResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerUnitTest {

    @Mock
    private SaveCategoryUseCase<CategoryDTO> saveCategoryUseCase;

    @InjectMocks
    private CategoryController categoryController;

    private SaveCategoryRequest request;
    private CategoryDTO savedCategory;

    @BeforeEach
    void setUp() {
        request = SaveCategoryRequest.builder()
                .name("Food")
                .icon("food-icon")
                .build();

        savedCategory = CategoryDTO.builder()
                .id(123L)
                .name("Food")
                .icon("food-icon")
                .build();
    }

    @Test
    void saveCategory_shouldReturn201AndValidResponse() {
        // Arrange
        when(saveCategoryUseCase.saveCategory(request)).thenReturn(savedCategory);

        // Act
        ResponseEntity<CustomResponse> responseEntity = categoryController.saveCategory(request);

        // Assert
        assertEquals(201, responseEntity.getStatusCode().value());

        CustomResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals("Category saved successfully", body.getMessage());

        Map<String, Object> data = body.getData();
        assertNotNull(data);
        assertTrue(data.containsKey("category"));

        CategoryDTO returned = (CategoryDTO) data.get("category");
        assertEquals(123L, returned.getId());
        assertEquals("Food", returned.getName());
        assertEquals("food-icon", returned.getIcon());

        verify(saveCategoryUseCase, times(1)).saveCategory(request);
    }
}
