package com.expensetrackerapp.infrastructure.inbound.controller.rest;

import com.expensetrackerapp.application.port.in.Category.GetCategories.GetCategoriesFilters;
import com.expensetrackerapp.application.port.in.Category.GetCategories.GetCategoriesUseCase;
import com.expensetrackerapp.application.port.in.Category.SaveCategory.SaveCategoryUseCase;
import com.expensetrackerapp.dto.CategoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private SaveCategoryUseCase<CategoryDTO> saveCategoryUseCase;

    @Mock
    private GetCategoriesUseCase<CategoryDTO, GetCategoriesFilters> getCategoryUseCase;

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
}
