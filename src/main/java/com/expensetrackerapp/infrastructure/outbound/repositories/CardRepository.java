package com.expensetrackerapp.infrastructure.outbound.repositories;

import com.expensetrackerapp.domain.enums.CardType;
import com.expensetrackerapp.infrastructure.outbound.entities.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    @Query("SELECT c FROM CardEntity c WHERE " +
            "(:cardId IS NULL OR c.id = :cardId) AND " +
            "(:name IS NULL OR c.name LIKE %:name%) AND " +
            "(:type IS NULL OR c.type = :type) AND " +
            "(:lastDigits IS NULL OR c.lastDigits LIKE %:lastDigits%) AND " +
            "(:bankName IS NULL OR c.bankName LIKE %:bankName%)")
    List<CardEntity> findAllCardsByFilters(
            @Param("cardId") Long cardId,
            @Param("name") String name,
            @Param("type") CardType type,
            @Param("lastDigits") String lastDigits,
            @Param("bankName") String bankName
    );
}
