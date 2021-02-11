package com.stuartbeard.iorek.external.hibp;

import com.stuartbeard.iorek.external.hibp.exception.HIBPNotFoundException;
import com.stuartbeard.iorek.external.hibp.mapper.HIBPResponseMapper;
import com.stuartbeard.iorek.external.hibp.service.HIBPService;
import com.stuartbeard.iorek.service.BreachService;
import com.stuartbeard.iorek.service.model.BreachInformation;
import com.stuartbeard.iorek.service.model.PasteInformation;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.util.List;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
public class HIBPClient implements BreachService {

    private final HIBPService hibpService;
    private final HIBPResponseMapper hibpResponseMapper;

    @Override
    public List<BreachInformation> getBreachInformation(@Nonnull String emailAddress) {
        try {
            return hibpResponseMapper.fromBreaches(hibpService.getBreaches(emailAddress));
        } catch (HIBPNotFoundException e) {
            return emptyList();
        }
    }

    @Override
    public List<PasteInformation> getPasteInformation(@Nonnull String emailAddress) {
        try {
            return hibpResponseMapper.fromPastes(hibpService.getPastes(emailAddress));
        } catch (HIBPNotFoundException e) {
            return emptyList();
        }
    }
}
