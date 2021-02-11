package com.stuartbeard.iorek.function.breach;

import com.stuartbeard.iorek.service.model.IdentityInformation;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

public class BreachFunctionHandler extends SpringBootRequestHandler<String, IdentityInformation> {
}
