package com.expensetrackerapp.application.port.out.Category;

import java.util.Optional;

public interface GetCategoryByIdOutboundPort <T> {
    Optional<T> getCategoryById(Long id);
}