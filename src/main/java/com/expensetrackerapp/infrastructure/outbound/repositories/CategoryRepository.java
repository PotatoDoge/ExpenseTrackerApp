package com.expensetrackerapp.infrastructure.outbound.repositories;

import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
