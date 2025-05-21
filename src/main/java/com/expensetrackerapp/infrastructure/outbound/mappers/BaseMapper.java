package com.expensetrackerapp.infrastructure.outbound.mappers;

public interface BaseMapper<POJO, ENTITY, DTO> {
    ENTITY fromPojoToEntity(POJO pojo);
    DTO fromEntityToDTO(ENTITY entity);
    POJO fromEntityToPOJO(ENTITY entity);

}