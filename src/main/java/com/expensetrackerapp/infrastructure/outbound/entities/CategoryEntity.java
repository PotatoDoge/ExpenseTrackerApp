package com.expensetrackerapp.infrastructure.outbound.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "expenses")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String icon;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<ExpenseEntity> expenses;

}
