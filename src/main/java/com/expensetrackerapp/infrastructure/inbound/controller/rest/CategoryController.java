package com.expensetrackerapp.infrastructure.inbound.controller.rest;

import com.expensetrackerapp.application.port.in.Category.DeleteCategory.DeleteCategoryUseCase;
import com.expensetrackerapp.application.port.in.Category.GetCategories.GetCategoriesFilters;
import com.expensetrackerapp.application.port.in.Category.GetCategories.GetCategoriesUseCase;
import com.expensetrackerapp.application.port.in.Category.SaveCategory.SaveCategoryRequest;
import com.expensetrackerapp.application.port.in.Category.SaveCategory.SaveCategoryUseCase;
import com.expensetrackerapp.application.port.in.Category.UpdateCategory.UpdateCategoryRequest;
import com.expensetrackerapp.application.port.in.Category.UpdateCategory.UpdateCategoryUseCase;
import com.expensetrackerapp.application.port.in.Expense.UpdateExpense.UpdateExpenseRequest;
import com.expensetrackerapp.dto.CategoryDTO;
import com.expensetrackerapp.shared.CustomResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
@Log4j2
public class CategoryController {

    private final SaveCategoryUseCase<CategoryDTO> saveCategoryUseCase;
    private final GetCategoriesUseCase<CategoryDTO, GetCategoriesFilters> getCategoriesUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final UpdateCategoryUseCase<CategoryDTO> updateCategoryUseCase;

    @PostMapping
    public ResponseEntity<CustomResponse> saveCategory(@RequestBody SaveCategoryRequest saveCategoryRequest){
        log.info("Received request to save category: name={}",
                saveCategoryRequest.getName());
        CategoryDTO savedCategory = saveCategoryUseCase.saveCategory(saveCategoryRequest);
        CustomResponse response = CustomResponse
                .builder()
                .timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now()))
                .message("Category saved successfully")
                .data(Map.of("category", savedCategory)).build();
        log.info("Category saved successfully with ID: {}", savedCategory.getId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<CustomResponse> getCategories(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String categoryName
    ){
        GetCategoriesFilters filters = GetCategoriesFilters
                .builder()
                .categoryId(categoryId)
                .categoryName(categoryName)
                .build();

        log.info("Received request to get all categories with the following filters: {}", filters);
        List<CategoryDTO> categories = getCategoriesUseCase.getCategories(filters);
        CustomResponse response = CustomResponse
                .builder()
                .timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now()))
                .message("Categories fetched successfully")
                .data(Map.of("categories", categories)).build();
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<CustomResponse> deleteCategory(@PathVariable Long categoryId){
        log.info("Received request to delete category with ID: {}", categoryId);
        deleteCategoryUseCase.deleteCategory(categoryId);
        CustomResponse response = CustomResponse
                .builder()
                .timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now()))
                .message("Category deleted successfully")
                .build();
        return ResponseEntity.status(204).body(response);
    }

    @PutMapping("{categoryId}")
    public ResponseEntity<CustomResponse> updateCategory(@RequestBody UpdateCategoryRequest updateCategoryRequest, @PathVariable Long categoryId){
        log.info("Received request to update category with ID: {}", categoryId);
        CategoryDTO updatedCategory = updateCategoryUseCase.updateCategory(updateCategoryRequest, categoryId);
        CustomResponse response = CustomResponse
                .builder()
                .timestamp(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now()))
                .message("Category updated successfully")
                .data(Map.of("category", updatedCategory)).build();
        return ResponseEntity.status(200).body(response);
    }
}
