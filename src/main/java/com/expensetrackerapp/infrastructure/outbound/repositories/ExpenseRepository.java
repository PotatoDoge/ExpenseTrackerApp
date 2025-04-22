package com.expensetrackerapp.infrastructure.outbound.repositories;

import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
}
