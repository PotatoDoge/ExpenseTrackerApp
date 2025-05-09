package com.expensetrackerapp.application.port.in.Category.UpdateCategory;

import com.expensetrackerapp.application.port.base.BaseCategoryRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UpdateCategoryRequest extends BaseCategoryRequest {
}
