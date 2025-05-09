package com.expensetrackerapp.infrastructure.outbound.adapters.Category;

import com.expensetrackerapp.application.port.in.Category.GetCategories.GetCategoriesFilters;
import com.expensetrackerapp.application.port.out.Category.GetCategoriesOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
@AllArgsConstructor
public class GetCategoriesRepository implements GetCategoriesOutboundPort<CategoryEntity, GetCategoriesFilters> {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryEntity> getCategories(GetCategoriesFilters filters) {
        List<CategoryEntity> categoryEntities =
                categoryRepository.findAllCategoriesByFilters(
                        filters.getCategoryId(),
                        filters.getCategoryName()
                );
        log.info("Fetched categories: {}", categoryEntities.size());
        return categoryEntities;
    }
}
