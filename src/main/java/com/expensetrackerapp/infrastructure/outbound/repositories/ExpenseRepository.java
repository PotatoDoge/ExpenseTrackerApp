package com.expensetrackerapp.infrastructure.outbound.repositories;

import com.expensetrackerapp.infrastructure.outbound.entities.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    @Query("SELECT e FROM ExpenseEntity e WHERE (:id IS NULL OR e.id = :id) " +
            "AND (:name IS NULL OR e.name LIKE %:name%) " +
            "AND (:description IS NULL OR e.description LIKE %:description%) " +
            "AND (:amount IS NULL OR e.amount = :amount) " +
            "AND (:currency IS NULL OR e.currency = :currency) " +
            "AND (DATE(:expenseDate) IS NULL OR DATE(e.expenseDate) = DATE(:expenseDate)) " +
            "AND (:requiresInvoice IS NULL OR e.requiresInvoice = :requiresInvoice) " +
            "AND (:installments IS NULL OR e.installments = :installments) " +
            "AND (:vendor IS NULL OR e.vendor LIKE %:vendor%) " +
            "AND (:location IS NULL OR e.location LIKE %:location%) " +
            "AND (:isPaidInFull IS NULL OR e.isPaidInFull = :isPaidInFull)")
    List<ExpenseEntity> findAllByFilters(
            Long id,
            String name,
            String description,
            BigDecimal amount,
            String currency,
            @Param("expenseDate") LocalDate expenseDate,
            Boolean requiresInvoice,
            Integer installments,
            String vendor,
            String location,
            Boolean isPaidInFull
    );
}
