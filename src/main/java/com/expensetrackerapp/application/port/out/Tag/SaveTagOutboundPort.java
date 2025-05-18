package com.expensetrackerapp.application.port.out.Tag;

import com.expensetrackerapp.domain.model.Tag;

public interface SaveTagOutboundPort <T>{
    T saveTag(Tag tag);
}
