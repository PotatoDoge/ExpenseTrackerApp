package com.expensetrackerapp.infrastructure.outbound.adapters.Category;

import com.expensetrackerapp.application.port.out.Category.DeleteCategoryOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class DeleteCategoryRepository implements DeleteCategoryOutboundPort {

    private final CategoryRepository categoryRepository;

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
        log.info("Category with id: {} deleted successfully", categoryId);
    }
}
