package com.expensetrackerapp.application.service.Category;

import com.expensetrackerapp.application.port.in.Category.GetCategories.GetCategoriesFilters;
import com.expensetrackerapp.application.port.in.Category.GetCategories.GetCategoriesUseCase;
import com.expensetrackerapp.application.port.out.Category.GetCategoriesOutboundPort;
import com.expensetrackerapp.dto.CategoryDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CategoryMapper;
import com.expensetrackerapp.shared.exceptions.DatabaseInteractionException;
import com.expensetrackerapp.shared.exceptions.MappingException;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class GetCategoriesService implements GetCategoriesUseCase <CategoryDTO, GetCategoriesFilters> {

    private final GetCategoriesOutboundPort<CategoryEntity, GetCategoriesFilters> getCategoriesPort;
    private final CategoryMapper expenseMapper;

    @Override
    public List<CategoryDTO> getCategories(GetCategoriesFilters filter) {
        try{
            return getCategoriesPort.getCategories(filter).stream().map(expenseMapper::fromEntityToDTO).toList();
        }
        catch (MappingException e) {
            throw new MappingException("Error while mapping category from entity to DTO: " + e.getClass().getSimpleName());
        }
        catch (IllegalArgumentException | PersistenceException | DataAccessException |
               NullPointerException | ClassCastException e) {
            log.error("Error occurred while fetching categories: {} - {}", e.getClass().getSimpleName(), e.getMessage(), e);
            throw new DatabaseInteractionException("Error while fetching categories: " + e.getClass().getSimpleName());
        }
        catch (Exception e) {
            log.error("Error occurred while fetching categories: {} - {}", e.getClass().getSimpleName(), e.getMessage(), e);
            throw new DatabaseInteractionException("Unhandled error while fetching all categories.");
        }
    }
}
