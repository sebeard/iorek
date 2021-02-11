package com.stuartbeard.iorek.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @ApiModelProperty(value = "A default message that describes the error or message")
    private String message;
}
