package com.expensetrackerapp.application.port.in.Category.GetCategories;

import java.util.List;

public interface GetCategoriesUseCase <T,F>{
    List<T> getCategories(F filter);
}
