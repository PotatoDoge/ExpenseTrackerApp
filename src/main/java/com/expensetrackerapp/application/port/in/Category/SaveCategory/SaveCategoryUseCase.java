package com.expensetrackerapp.application.port.in.Category.SaveCategory;

public interface SaveCategoryUseCase <T> {
    T saveCategory(SaveCategoryRequest request);
}
