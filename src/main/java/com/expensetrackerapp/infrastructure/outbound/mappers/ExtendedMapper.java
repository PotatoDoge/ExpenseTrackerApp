package com.expensetrackerapp.infrastructure.outbound.mappers;

public interface ExtendedMapper<POJO, ENTITY, DTO, REQUEST> extends BaseMapper<POJO, ENTITY, DTO> {
    POJO fromRequestToPojo(REQUEST request);
}