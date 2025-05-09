package com.expensetrackerapp.application.port.in.Category.UpdateCategory;

public interface UpdateCategoryUseCase <T>{
    T updateCategory(UpdateCategoryRequest request, Long categoryId);
}
