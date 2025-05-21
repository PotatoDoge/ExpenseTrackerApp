package com.expensetrackerapp.infrastructure.outbound.adapters.Tag;

import com.expensetrackerapp.application.port.out.Tag.SaveTagOutboundPort;
import com.expensetrackerapp.domain.model.Tag;
import com.expensetrackerapp.infrastructure.outbound.entities.TagEntity;
import com.expensetrackerapp.infrastructure.outbound.mappers.TagMapper;
import com.expensetrackerapp.infrastructure.outbound.repositories.TagRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class SaveTagRepository implements SaveTagOutboundPort<TagEntity> {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public TagEntity saveTag(Tag tag) {
        TagEntity newTag = tagRepository.save(tagMapper.fromPojoToEntity(tag));
        log.info("Saved tag: {}", newTag.getName());
        return newTag;
    }
}
