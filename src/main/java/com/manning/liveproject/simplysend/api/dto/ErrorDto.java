package com.manning.liveproject.simplysend.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.manning.liveproject.simplysend.api.enums.ErrorCode;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@ApiModel("Error")
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorDto {

    private LocalDateTime timestamp;
    private ErrorCode code;
    private String message;
    @Singular
    private Map<String, String> violations;
}
