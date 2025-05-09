package com.expensetrackerapp.application.port.out.Category;

import java.util.List;

public interface GetCategoriesOutboundPort <T,F> {
    List<T> getCategories(F filters);
}
