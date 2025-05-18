package com.expensetrackerapp.infrastructure.outbound.mappers;

import com.expensetrackerapp.domain.model.Tag;
import com.expensetrackerapp.dto.TagDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.TagEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;

@Log4j2
@Component
public class TagMapper implements BaseMapper<Tag, TagEntity, TagDTO>{

    @Override
    public TagEntity fromPojoToEntity(Tag tag) {
        return TagEntity
                .builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .build();
    }

    @Override
    public TagDTO fromEntityToDTO(TagEntity tagEntity) {
        return TagDTO
                .builder()
                .color(tagEntity.getColor())
                .name(tagEntity.getName())
                .id(tagEntity.getId())
                .build();
    }

    @Override
    public Tag fromEntityToPOJO(TagEntity tagEntity) {
        return Tag.builder()
                .id(tagEntity.getId())
                .name(tagEntity.getName())
                .color(tagEntity.getColor())
                .build();
    }

    public Tag fromMapEntryToTag(Map.Entry<String, String> tagMap) {
        return Tag.builder()
                .name(tagMap.getKey().toUpperCase())
                .color(tagMap.getValue())
                .build();
    }

}
