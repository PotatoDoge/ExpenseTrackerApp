package com.expensetrackerapp.application.port.out;

import com.expensetrackerapp.domain.model.Category;

public interface SaveCategoryOutboundPort <T> {
    T saveCategory(Category category);
}
