package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.ArrayList;
import java.util.List;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JsonServiceTest {
    static JsonService jsonService;

    @BeforeAll
    static void initJSONService() {
        jsonService = new JsonService();
    }

    @Test
    public void jsonToModulTest() {
        String testjson = "{\"id\":5,\"veranstaltungen\":[{\"id\":3}]," +
                "\"modulkategorie\":\"MASTERARBEIT\",\"sichtbar\":false}";
        Modul testmodul = jsonService.jsonObjectToModul(testjson);

        Modul vergleichsmodul = new Modul();
        vergleichsmodul.setModulkategorie(Modulkategorie.MASTERARBEIT);
        Veranstaltung veranstaltung = new Veranstaltung();
        veranstaltung.setId((long) 3);
        List<Veranstaltung> veranstaltungList = new ArrayList<Veranstaltung>();
        veranstaltungList.add(veranstaltung);
        vergleichsmodul.setVeranstaltungen(veranstaltungList);
        vergleichsmodul.setId((long) 5);

        assertThat(vergleichsmodul.getModulkategorie()).isEqualTo(testmodul.getModulkategorie());
    }
}
