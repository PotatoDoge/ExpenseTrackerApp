package com.expensetrackerapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagDTO {
    private Long id;
    private String name;
    private String color;
}
