package com.expensetrackerapp.application.port.out.Category;

import com.expensetrackerapp.domain.model.Category;

public interface SaveCategoryOutboundPort <T> {
    T saveCategory(Category category);
}
