package com.expensetrackerapp.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CustomResponse {

    private String timestamp;
    private String message;
    private String developerMessage;
    private Map<String, Object> data;

}