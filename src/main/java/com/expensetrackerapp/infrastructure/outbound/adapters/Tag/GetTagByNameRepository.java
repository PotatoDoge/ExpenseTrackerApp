package com.expensetrackerapp.infrastructure.outbound.adapters.Tag;

import com.expensetrackerapp.application.port.out.Tag.GetTagByNameOutboundPort;
import com.expensetrackerapp.infrastructure.outbound.entities.TagEntity;
import com.expensetrackerapp.infrastructure.outbound.repositories.TagRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
@Log4j2
public class GetTagByNameRepository implements GetTagByNameOutboundPort<TagEntity> {

    private final TagRepository tagRepository;

    @Override
    public Optional<TagEntity> getTagByName(String name) {
        Optional<TagEntity> tagEntity = tagRepository.findByName(name);
        if(tagEntity.isEmpty()) {
            log.info("Tag with name: {} does not exist", name);
        }
        else{
            log.info("Tag with name: {} found", name);
        }
        return tagRepository.findByName(name);
    }
}
