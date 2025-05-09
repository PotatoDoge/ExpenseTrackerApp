package com.expensetrackerapp.application.port;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class BaseCategoryRequest {
    private String name;
    private String icon;
}
