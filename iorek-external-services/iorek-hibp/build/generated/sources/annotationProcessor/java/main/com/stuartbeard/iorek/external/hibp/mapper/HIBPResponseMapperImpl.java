package com.stuartbeard.iorek.external.hibp.mapper;

import com.stuartbeard.iorek.external.hibp.model.Breach;
import com.stuartbeard.iorek.external.hibp.model.Paste;
import com.stuartbeard.iorek.service.model.BreachInformation;
import com.stuartbeard.iorek.service.model.PasteInformation;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-02-11T08:43:27+0000",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-6.8.2.jar, environment: Java 11.0.1 (Oracle Corporation)"
)
public class HIBPResponseMapperImpl implements HIBPResponseMapper {

    @Override
    public BreachInformation fromBreach(Breach breach) {
        if ( breach == null ) {
            return null;
        }

        BreachInformation breachInformation = new BreachInformation();

        List<String> list = breach.getDataClasses();
        if ( list != null ) {
            breachInformation.setInformationBreached( new ArrayList<String>( list ) );
        }
        breachInformation.setName( breach.getName() );
        breachInformation.setDomain( breach.getDomain() );
        breachInformation.setBreachDate( breach.getBreachDate() );

        return breachInformation;
    }

    @Override
    public List<BreachInformation> fromBreaches(List<Breach> breaches) {
        if ( breaches == null ) {
            return null;
        }

        List<BreachInformation> list = new ArrayList<BreachInformation>( breaches.size() );
        for ( Breach breach : breaches ) {
            list.add( fromBreach( breach ) );
        }

        return list;
    }

    @Override
    public PasteInformation fromPaste(Paste paste) {
        if ( paste == null ) {
            return null;
        }

        PasteInformation pasteInformation = new PasteInformation();

        pasteInformation.setAdded( paste.getDate() );
        pasteInformation.setTitle( paste.getTitle() );

        return pasteInformation;
    }

    @Override
    public List<PasteInformation> fromPastes(List<Paste> pastes) {
        if ( pastes == null ) {
            return null;
        }

        List<PasteInformation> list = new ArrayList<PasteInformation>( pastes.size() );
        for ( Paste paste : pastes ) {
            list.add( fromPaste( paste ) );
        }

        return list;
    }
}
