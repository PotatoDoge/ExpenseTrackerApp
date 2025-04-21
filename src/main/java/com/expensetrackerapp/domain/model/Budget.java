package com.expensetrackerapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {

    private Long id;
    private BigDecimal limitAmount;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<Category> categories;

}
