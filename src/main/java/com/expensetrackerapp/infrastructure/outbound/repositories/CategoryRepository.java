package com.expensetrackerapp.infrastructure.outbound.repositories;

import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("SELECT c FROM CategoryEntity c WHERE (:id IS NULL OR c.id = :id) " +
            "AND (:name IS NULL OR c.name LIKE %:name%)")
    List<CategoryEntity> findAllCategoriesByFilters(
            @Param("id") Long id,
            @Param("name") String name);
}
