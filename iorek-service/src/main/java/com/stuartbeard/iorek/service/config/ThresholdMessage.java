package com.stuartbeard.iorek.service.config;

import lombok.Data;

@Data
public class ThresholdMessage {

    private int threshold = 0;
    private String message = "";
}
