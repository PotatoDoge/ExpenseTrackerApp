package com.expensetrackerapp.infrastructure.outbound.adapters.Category;

import com.expensetrackerapp.application.port.out.Category.GetCategoryByIdOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
@Log4j2
public class GetCategoryByIdRepository implements GetCategoryByIdOutboundPort<CategoryEntity> {

    private final CategoryRepository categoryRepository;

    @Override
    public Optional<CategoryEntity> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
}
