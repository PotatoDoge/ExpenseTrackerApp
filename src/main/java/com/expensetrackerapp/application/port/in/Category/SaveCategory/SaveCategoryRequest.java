package com.expensetrackerapp.application.port.in.Category.SaveCategory;

import com.expensetrackerapp.application.port.base.BaseCategoryRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SaveCategoryRequest extends BaseCategoryRequest {
}
