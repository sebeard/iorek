package com.stuartbeard.iorek.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @ApiModelProperty(value = "A default message that describes the error or message", readOnly = true)
    private String message;
}
