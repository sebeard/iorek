package com.stuartbeard.iorek.external.hibp.mapper;

import com.stuartbeard.iorek.external.hibp.model.Breach;
import com.stuartbeard.iorek.external.hibp.model.Paste;
import com.stuartbeard.iorek.service.model.BreachInformation;
import com.stuartbeard.iorek.service.model.PasteInformation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface HIBPResponseMapper {

    @Mapping(target = "informationBreached", source = "dataClasses")
    BreachInformation fromBreach(Breach breach);

    List<BreachInformation> fromBreaches(List<Breach> breaches);

    @Mapping(target = "added", source = "date")
    PasteInformation fromPaste(Paste paste);

    List<PasteInformation> fromPastes(List<Paste> pastes);

}
