package com.stuartbeard.iorek.service.config;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ThresholdMessage {

    private int threshold = 0;
    private String message = "";
}
