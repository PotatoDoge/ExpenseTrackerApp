package com.expensetrackerapp.infrastructure.outbound.mappers;

import com.expensetrackerapp.domain.model.Tag;
import com.expensetrackerapp.dto.TagDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.TagEntity;
import com.expensetrackerapp.shared.exceptions.MappingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;

@Log4j2
@Component
public class TagMapper implements BaseMapper<Tag, TagEntity, TagDTO>{

    @Override
    public TagEntity fromPojoToEntity(Tag tag) {
        try{
            return TagEntity
                    .builder()
                    .id(tag.getId())
                    .name(tag.getName())
                    .color(tag.getColor())
                    .build();
        }
        catch (Exception ex) {
            log.error("Error occurred while mapping object to entity: {}", ex.getMessage(), ex);
            throw new MappingException("Error while mapping object to entity: " + ex);
        }
    }

    @Override
    public TagDTO fromEntityToDTO(TagEntity tagEntity) {
        try{
            return TagDTO
                    .builder()
                    .color(tagEntity.getColor())
                    .name(tagEntity.getName())
                    .id(tagEntity.getId())
                    .build();
        }
        catch (Exception ex) {
            log.error("Error occurred while mapping entity to dto: {}", ex.getMessage(), ex);
            throw new MappingException("Error while mapping entity to dto: " + ex);
        }
    }

    @Override
    public Tag fromEntityToPOJO(TagEntity tagEntity) {
        try{
            return Tag.builder()
                    .id(tagEntity.getId())
                    .name(tagEntity.getName())
                    .color(tagEntity.getColor())
                    .build();
        }
        catch (Exception ex) {
            log.error("Error occurred while mapping entity to pojo: {}", ex.getMessage(), ex);
            throw new MappingException("Error while mapping entity to pojo: " + ex);
        }
    }

    public Tag fromMapEntryToTag(Map.Entry<String, String> tagMap) {
        try{
            return Tag.builder()
                    .name(tagMap.getKey().toUpperCase())
                    .color(tagMap.getValue())
                    .build();
        }
        catch (Exception ex) {
            log.error("Error occurred while mapping map entry to pojo: {}", ex.getMessage(), ex);
            throw new MappingException("Error while mapping map entry to pojo: " + ex);
        }
    }

}
