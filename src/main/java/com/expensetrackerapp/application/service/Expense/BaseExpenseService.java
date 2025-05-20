package com.expensetrackerapp.application.service.Expense;

import com.expensetrackerapp.application.port.out.Category.GetCategoryByIdOutboundPort;
import com.expensetrackerapp.application.port.out.Tag.GetTagByNameOutboundPort;
import com.expensetrackerapp.application.port.out.Tag.SaveTagOutboundPort;
import com.expensetrackerapp.domain.model.Category;
import com.expensetrackerapp.domain.model.Tag;
import com.expensetrackerapp.infrastructure.outbound.entities.CategoryEntity;
import com.expensetrackerapp.infrastructure.outbound.entities.TagEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.CategoryMapper;
import com.expensetrackerapp.infrastructure.outbound.mappers.TagMapper;
import com.expensetrackerapp.shared.exceptions.NotFoundInDatabase;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.expensetrackerapp.shared.constants.Colors.WHITE;

public abstract class BaseExpenseService {

    protected final GetCategoryByIdOutboundPort<CategoryEntity> getCategoryByIdRepository;
    protected final GetTagByNameOutboundPort<TagEntity> getTagByNameRepository;
    protected final SaveTagOutboundPort<TagEntity> saveTagRepository;
    protected final CategoryMapper categoryMapper;
    protected final TagMapper tagMapper;

    public BaseExpenseService(GetCategoryByIdOutboundPort<CategoryEntity> getCategoryByIdRepository,
                              GetTagByNameOutboundPort<TagEntity> getTagByNameRepository,
                              SaveTagOutboundPort<TagEntity> saveTagRepository,
                              CategoryMapper categoryMapper,
                              TagMapper tagMapper) {
        this.getCategoryByIdRepository = getCategoryByIdRepository;
        this.getTagByNameRepository = getTagByNameRepository;
        this.saveTagRepository = saveTagRepository;
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
    }

    protected Category validateAndMapCategory(Long categoryId) {
        if (categoryId == null) return null;

        return getCategoryByIdRepository.getCategoryById(categoryId)
                .map(categoryMapper::fromEntityToPOJO)
                .orElseThrow(() -> new NotFoundInDatabase("Category not found with id: " + categoryId));
    }

    protected Set<Tag> validateAndMapTags(Map<String, String> tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptySet();
        }

        return tags.entrySet().stream()
                .filter(entry -> entry.getKey() != null && !entry.getKey().isBlank()) // skip null/blank keys
                .map(entry -> {
                    String tagName = entry.getKey().toUpperCase();
                    String tagColor = (entry.getValue() == null || entry.getValue().isBlank())
                            ? WHITE
                            : entry.getValue().trim();
                    Optional<TagEntity> tagEntityOpt = getTagByNameRepository.getTagByName(tagName);
                    TagEntity tagEntity = tagEntityOpt.orElseGet(() ->
                            saveTagRepository.saveTag(tagMapper.fromMapEntryToTag(Map.entry(tagName, tagColor)))
                    );
                    return tagMapper.fromEntityToPOJO(tagEntity);
                })
                .collect(Collectors.toSet());
    }
}

