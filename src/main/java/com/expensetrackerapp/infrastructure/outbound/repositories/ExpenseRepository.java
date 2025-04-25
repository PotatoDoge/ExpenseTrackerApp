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
            "AND (e.expenseDate = COALESCE(:expenseDate, e.expenseDate) ) " +
            "AND (:requiresInvoice IS NULL OR e.requiresInvoice = :requiresInvoice) " +
            "AND (:installments IS NULL OR e.installments = :installments) " +
            "AND (:vendor IS NULL OR e.vendor LIKE %:vendor%) " +
            "AND (:location IS NULL OR e.location LIKE %:location%) " +
            "AND (:isPaidInFull IS NULL OR e.isPaidInFull = :isPaidInFull)")
    List<ExpenseEntity> findAllExpensesByFilters(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("description") String description,
            @Param("amount") BigDecimal amount,
            @Param("currency") String currency,
            @Param("expenseDate") LocalDate expenseDate,
            @Param("requiresInvoice") Boolean requiresInvoice,
            @Param("installments") Integer installments,
            @Param("vendor") String vendor,
            @Param("location") String location,
            @Param("isPaidInFull") Boolean isPaidInFull
    );
}
