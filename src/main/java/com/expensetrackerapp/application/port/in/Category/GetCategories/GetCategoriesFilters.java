package com.expensetrackerapp.application.port.in.Category.GetCategories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoriesFilters {
    private Long categoryId;
    private String categoryName;
}
