package com.expensetrackerapp.infrastructure.outbound.mappers;

import com.expensetrackerapp.domain.model.Tag;
import com.expensetrackerapp.dto.TagDTO;
import com.expensetrackerapp.infrastructure.outbound.entities.TagEntity;
import com.expensetrackerapp.shared.exceptions.MappingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TagMapperTest {

    private TagMapper tagMapper;

    @BeforeEach
    void setUp() {
        tagMapper = new TagMapper();
    }

    @Test
    void testFromPojoToEntity() {
        Tag tag = Tag.builder().id(1L).name("Work").color("Blue").build();

        TagEntity tagEntity = tagMapper.fromPojoToEntity(tag);

        assertNotNull(tagEntity);
        assertEquals(tag.getId(), tagEntity.getId());
        assertEquals(tag.getName(), tagEntity.getName());
        assertEquals(tag.getColor(), tagEntity.getColor());
    }

    @Test
    void testFromEntityToDTO() {
        TagEntity tagEntity = TagEntity.builder().id(2L).name("Personal").color("Red").build();

        TagDTO tagDTO = tagMapper.fromEntityToDTO(tagEntity);

        assertNotNull(tagDTO);
        assertEquals(tagEntity.getId(), tagDTO.getId());
        assertEquals(tagEntity.getName(), tagDTO.getName());
        assertEquals(tagEntity.getColor(), tagDTO.getColor());
    }

    @Test
    void testFromEntityToPOJO() {
        TagEntity tagEntity = TagEntity.builder().id(3L).name("Health").color("Green").build();

        Tag tag = tagMapper.fromEntityToPOJO(tagEntity);

        assertNotNull(tag);
        assertEquals(tagEntity.getId(), tag.getId());
        assertEquals(tagEntity.getName(), tag.getName());
        assertEquals(tagEntity.getColor(), tag.getColor());
    }

    @Test
    void testFromMapEntryToTag() {
        Map.Entry<String, String> mapEntry = new AbstractMap.SimpleEntry<>("fun", "Yellow");

        Tag tag = tagMapper.fromMapEntryToTag(mapEntry);

        assertNotNull(tag);
        assertEquals("FUN", tag.getName());
        assertEquals("Yellow", tag.getColor());
        assertNull(tag.getId()); // ID not set in map entry
    }

    @Test
    void testFromPojoToEntityThrowsExceptionOnNull() {
        MappingException exception = assertThrows(MappingException.class, () -> {
            tagMapper.fromPojoToEntity(null);
        });

        assertTrue(exception.getMessage().contains("Error while mapping object to entity"));
    }

    @Test
    void testFromEntityToDTOThrowsExceptionOnNull() {
        MappingException exception = assertThrows(MappingException.class, () -> {
            tagMapper.fromEntityToDTO(null);
        });

        assertTrue(exception.getMessage().contains("Error while mapping entity to dto"));
    }

    @Test
    void testFromEntityToPOJOThrowsExceptionOnNull() {
        MappingException exception = assertThrows(MappingException.class, () -> {
            tagMapper.fromEntityToPOJO(null);
        });

        assertTrue(exception.getMessage().contains("Error while mapping entity to pojo"));
    }

    @Test
    void testFromMapEntryToTagThrowsExceptionOnNull() {
        MappingException exception = assertThrows(MappingException.class, () -> {
            tagMapper.fromMapEntryToTag(null);
        });

        assertTrue(exception.getMessage().contains("Error while mapping map entry to pojo"));
    }

    @Test
    void testFromMapEntryToTagThrowsExceptionOnNullKey() {
        Map.Entry<String, String> invalidEntry = new AbstractMap.SimpleEntry<>(null, "Red");

        MappingException exception = assertThrows(MappingException.class, () -> {
            tagMapper.fromMapEntryToTag(invalidEntry);
        });

        assertTrue(exception.getMessage().contains("Error while mapping map entry to pojo"));
    }
}
