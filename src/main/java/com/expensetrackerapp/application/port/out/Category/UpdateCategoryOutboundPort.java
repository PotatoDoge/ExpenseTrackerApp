package com.expensetrackerapp.application.port.out.Category;

import com.expensetrackerapp.domain.model.Category;

public interface UpdateCategoryOutboundPort <T>{
    T updateCategory(Category category, Long categoryId);
}