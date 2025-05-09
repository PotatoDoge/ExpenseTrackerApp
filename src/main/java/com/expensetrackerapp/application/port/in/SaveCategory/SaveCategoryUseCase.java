package com.expensetrackerapp.application.port.in.SaveCategory;

public interface SaveCategoryUseCase <T> {
    T saveCategory(SaveCategoryRequest request);
}
