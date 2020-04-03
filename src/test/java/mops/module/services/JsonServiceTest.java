package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;

import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class JsonServiceTest {

    @Test
    public void jsonToModulTest() {
        final String testjson = "{\"id\":5,\"veranstaltungen\":[{\"id\":3}],"
                + "\"modulkategorie\":\"MASTERARBEIT\",\"sichtbar\":false}";
        final Modul testmodul = JsonService.jsonObjectToModul(testjson);

        Modul vergleichsmodul = new Modul();
        vergleichsmodul.setModulkategorie(Modulkategorie.MASTERARBEIT);
        Veranstaltung veranstaltung = new Veranstaltung();
        veranstaltung.setId((long) 3);
        vergleichsmodul.addVeranstaltung(veranstaltung);
        vergleichsmodul.setId((long) 5);

        assertThat(vergleichsmodul.getModulkategorie()).isEqualTo(testmodul.getModulkategorie());
    }
}
