package com.expensetrackerapp.application.port.out.Tag;

import java.util.Optional;

public interface GetTagByNameOutboundPort <T>{
    Optional<T> getTagByName(String name);
}
