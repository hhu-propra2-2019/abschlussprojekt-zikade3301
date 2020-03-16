package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.repositories.AntragRepository;
import mops.module.repositories.ModulSnapshotRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class ModulServiceTest {
    private ModulService modulService;

    private AntragRepository antragRepository;
    private ModulSnapshotRepository modulSnapshotRepository;

    private String modul1;
    private String modul2;
    private String modul3;
    private String modul4;
    private String diffs1;
    private String diffs2;

    /**
     *
     */
    @BeforeEach
    public void init() {
        antragRepository = mock(AntragRepository.class);
        modulSnapshotRepository = mock(ModulSnapshotRepository.class);
        modulService = new ModulService(antragRepository, modulSnapshotRepository);

        modul1 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3}],"
                + "\"modulkategorie\":\"MASTERARBEIT\"}";
        modul2 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3}],"
                + "\"modulkategorie\":\"BACHELORARBEIT\"}";
        modul3 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3,"
                + "\"voraussetzungenTeilnahme\":[]}],\"modulkategorie\":\"MASTERARBEIT\"}";
        modul4 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3,"
                + "\"voraussetzungenTeilnahme\":[\"Informatik I\"]}],"
                + "\"modulkategorie\":\"BACHELORARBEIT\"}";
        diffs1 = "{\"id\":5,"
                + "\"modulkategorie\":\"BACHELORARBEIT\"}";
        diffs2 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3,"
                + "\"voraussetzungenTeilnahme\":[\"Informatik I\"]}],"
                + "\"modulkategorie\":\"BACHELORARBEIT\"}";
    }

    @Test
    public void calculateModulDiffsTest1() {
        Modul diffs = modulService.calculateModulDiffs(JsonService.jsonObjectToModul(modul1),
                JsonService.jsonObjectToModul(modul2));
        try {
            JSONAssert.assertEquals(diffs1, JsonService.modulToJsonObject(diffs), false);
        } catch (JSONException e) {
            fail(e.toString());
        }
    }

    @Test
    public void calculateModulDiffsTest2() {
        Modul diffs = modulService.calculateModulDiffs(JsonService.jsonObjectToModul(modul3),
                JsonService.jsonObjectToModul(modul4));
        try {
            JSONAssert.assertEquals(diffs2, JsonService.modulToJsonObject(diffs), false);
        } catch (JSONException e) {
            fail(e.toString());
        }
    }

    @Test
    public void calculateModulDiffsNullTest() {
        Modul modul1 = new Modul();
        modul1.setModulkategorie(Modulkategorie.MASTERARBEIT);

        Modul modul2 = new Modul();
        modul2.setModulkategorie(Modulkategorie.MASTERARBEIT);

        Modul diffs = modulService.calculateModulDiffs(modul1, modul2);
        assertThat(diffs).isNull();
    }

    @Test
    public void applyAntragOnModulTest() {
        Modul modul = JsonService.jsonObjectToModul(modul1);
        Antrag antrag = new Antrag();
        antrag.setJsonModulAenderung(diffs1);
        modulService.applyAntragOnModul(modul, antrag);

        try {
            JSONAssert.assertEquals(modul2, JsonService.modulToJsonObject(modul), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}