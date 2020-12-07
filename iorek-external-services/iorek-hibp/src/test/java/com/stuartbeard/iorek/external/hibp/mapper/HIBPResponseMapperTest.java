package com.stuartbeard.iorek.external.hibp.mapper;

import com.stuartbeard.iorek.external.hibp.model.Breach;
import com.stuartbeard.iorek.external.hibp.model.Paste;
import com.stuartbeard.iorek.service.model.BreachInformation;
import com.stuartbeard.iorek.service.model.PasteInformation;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class HIBPResponseMapperTest {

    private static final LocalDate LOCAL_DATE = LocalDate.now();
    private static final ZonedDateTime ZONED_DATE_TIME = ZonedDateTime.now();
    private static final int EMAIL_COUNT = 1000;
    private static final String SOURCE = "source";
    private static final String TITLE = "title";
    private static final String ID = "id";
    private static final List<String> DATA_CLASSES = asList("emails", "passwords");
    private static final boolean SPAM_LIST = false;
    private static final boolean SENSITIVE = true;
    private static final boolean FABRICATED = false;
    private static final boolean VERIFIED = true;
    private static final boolean RETIRED = false;
    private static final int PWN_COUNT = 100000;
    private static final String LOGO_PATH = "logoPath";
    private static final String DESCRIPTION = "description";
    private static final String DOMAIN = "domain.com";
    private static final String NAME = "name";

    private final HIBPResponseMapper mapper = Mappers.getMapper(HIBPResponseMapper.class);

    @Test
    void shouldMapPasteToPasteInformation() {
        Paste paste = generatePaste();

        PasteInformation pasteInformation = mapper.fromPaste(paste);

        assertThat(pasteInformation.getTitle()).isEqualTo(TITLE);
    }

    @Test
    void shouldMapNullPasteToNullPasteInformation() {

        PasteInformation pasteInformation = mapper.fromPaste(null);

        assertThat(pasteInformation).isNull();
    }

    @Test
    void shouldMapBreachToBreachInformation() {
        Breach breach = generateBreach();

        BreachInformation breachInformation = mapper.fromBreach(breach);

        assertThat(breachInformation.getName()).isEqualTo(NAME);
        assertThat(breachInformation.getDomain()).isEqualTo(DOMAIN);
        assertThat(breachInformation.getInformationBreached()).isEqualTo(DATA_CLASSES);
    }

    @Test
    void shouldMapNullBreachToNullBreachInformation() {

        BreachInformation breachInformation = mapper.fromBreach(null);

        assertThat(breachInformation).isNull();
    }

    @Test
    void shouldMapListOfPastesToListOfPasteInformation() {
        List<Paste> pastes = asList(generatePaste(), generatePaste());

        List<PasteInformation> pasteInformationList = mapper.fromPastes(pastes);

        assertThat(pasteInformationList).hasSize(2);
        pasteInformationList.forEach(
            pasteInformation -> assertThat(pasteInformation.getTitle()).isEqualTo(TITLE)
        );
    }

    @Test
    void shouldMapNullPasteListToNullPasteInformationList() {

        List<PasteInformation> pasteInformationList = mapper.fromPastes(null);

        assertThat(pasteInformationList).isNull();
    }

    @Test
    void shouldMapListOfBreachesToListOfBreachInformation() {
        List<Breach> breaches = asList(generateBreach(), generateBreach());

        List<BreachInformation> breachInformationList = mapper.fromBreaches(breaches);

        assertThat(breachInformationList).hasSize(2);
        breachInformationList.forEach(
            breachInformation -> {
                assertThat(breachInformation.getName()).isEqualTo(NAME);
                assertThat(breachInformation.getDomain()).isEqualTo(DOMAIN);
                assertThat(breachInformation.getInformationBreached()).isEqualTo(DATA_CLASSES);
            }
        );
    }

    @Test
    void shouldMapNullBreachListToNullBreachInformationList() {

        List<BreachInformation> breachInformationList = mapper.fromBreaches(null);

        assertThat(breachInformationList).isNull();
    }

    private Paste generatePaste() {
        return new Paste()
            .setId(ID)
            .setTitle(TITLE)
            .setSource(SOURCE)
            .setEmailCount(EMAIL_COUNT)
            .setDate(LOCAL_DATE);
    }

    private Breach generateBreach() {
        return new Breach()
            .setName(NAME)
            .setTitle(TITLE)
            .setDomain(DOMAIN)
            .setDescription(DESCRIPTION)
            .setLogoPath(LOGO_PATH)
            .setPwnCount(PWN_COUNT)
            .setRetired(RETIRED)
            .setVerified(VERIFIED)
            .setFabricated(FABRICATED)
            .setSensitive(SENSITIVE)
            .setSpamList(SPAM_LIST)
            .setAddedDate(ZONED_DATE_TIME)
            .setBreachDate(LOCAL_DATE)
            .setModifiedDate(ZONED_DATE_TIME)
            .setDataClasses(DATA_CLASSES);
    }


}
