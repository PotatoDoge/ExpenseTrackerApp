package com.expensetrackerapp.application.port.in.SaveCategory;

import com.expensetrackerapp.application.port.BaseCategoryRequest;
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
