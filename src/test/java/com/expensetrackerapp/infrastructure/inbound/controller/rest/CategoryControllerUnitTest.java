package com.expensetrackerapp.infrastructure.inbound.controller.rest;

import com.expensetrackerapp.application.port.in.Category.DeleteCategory.DeleteCategoryUseCase;
import com.expensetrackerapp.application.port.in.Category.GetCategories.GetCategoriesFilters;
import com.expensetrackerapp.application.port.in.Category.GetCategories.GetCategoriesUseCase;
import com.expensetrackerapp.application.port.in.Category.SaveCategory.SaveCategoryUseCase;
import com.expensetrackerapp.application.port.in.Category.UpdateCategory.UpdateCategoryRequest;
import com.expensetrackerapp.application.port.in.Category.UpdateCategory.UpdateCategoryUseCase;
import com.expensetrackerapp.dto.CategoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private SaveCategoryUseCase<CategoryDTO> saveCategoryUseCase;

    @Mock
    private GetCategoriesUseCase<CategoryDTO, GetCategoriesFilters> getCategoryUseCase;

    @Mock
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Mock
    private UpdateCategoryUseCase<CategoryDTO> updateCategoryUseCase;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryDTO savedCategory;

    private String readJson(String path) throws IOException {
        return new String(
                Files.readAllBytes(Paths.get("src/test/resources/" + path))
        );
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        savedCategory = CategoryDTO.builder()
                .id(123L)
                .name("Food")
                .icon("food-icon")
                .build();
    }

    @Test
    void saveCategory_shouldReturn201AndValidResponse() throws Exception {

        String saveExpenseRequestAsJSON = readJson("messages/Expense/SaveExpenseRequest.json");

        when(saveCategoryUseCase.saveCategory(any())).thenReturn(savedCategory);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saveExpenseRequestAsJSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Category saved successfully"))
                .andExpect(jsonPath("$.data.category.id").value(123L))
                .andExpect(jsonPath("$.data.category.name").value("Food"))
                .andExpect(jsonPath("$.data.category.icon").value("food-icon"));
    }

    @Test
    void getCategories_shouldReturn200WithoutFilters() throws Exception {
        when(getCategoryUseCase.getCategories(any())).thenReturn(List.of(savedCategory));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Categories fetched successfully"))
                .andExpect(jsonPath("$.data.categories", hasSize(1)))
                .andExpect(jsonPath("$.data.categories[0].name").value("Food"));
    }

    @Test
    void getCategories_shouldReturn200WithFilters() throws Exception {
        when(getCategoryUseCase.getCategories(any())).thenReturn(List.of(savedCategory));

        mockMvc.perform(get("/categories")
                        .param("categoryId", "123")
                        .param("categoryName", "Food"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.categories[0].id").value(123L))
                .andExpect(jsonPath("$.data.categories[0].name").value("Food"));
    }

    @Test
    void getCategories_shouldReturnEmptyList() throws Exception {
        when(getCategoryUseCase.getCategories(any())).thenReturn(List.of());

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.categories", hasSize(0)));
    }

    @Test
    void deleteCategory_shouldReturn204AndCallUseCase() throws Exception {
        // Given
        Long categoryId = 1L;

        doNothing().when(deleteCategoryUseCase).deleteCategory(1L);

        // When & Then
        mockMvc.perform(delete("/categories/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Category deleted successfully"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(deleteCategoryUseCase, Mockito.times(1)).deleteCategory(categoryId);
    }

    @Test
    void updateCategory_shouldReturn200_whenUpdateIsSuccessful() throws Exception {
        Long categoryId = 1L;
        String updateCategoryRequestAsJson = readJson("messages/Category/UpdateCategoryRequest.json");

        CategoryDTO updatedCategory = CategoryDTO.builder()
                .id(categoryId)
                .name("Updated Category")
                .icon("updated-icon")
                .build();

        when(updateCategoryUseCase.updateCategory(any(UpdateCategoryRequest.class), eq(categoryId)))
                .thenReturn(updatedCategory);

        mockMvc.perform(put("/categories/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateCategoryRequestAsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Category updated successfully"))
                .andExpect(jsonPath("$.data.category.id").value(categoryId))
                .andExpect(jsonPath("$.data.category.name").value("Updated Category"))
                .andExpect(jsonPath("$.data.category.icon").value("updated-icon"));

        verify(updateCategoryUseCase).updateCategory(any(UpdateCategoryRequest.class), eq(categoryId));
    }

}
