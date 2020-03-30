package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.generator.ModulFaker;
import mops.module.repositories.AntragRepository;
import mops.module.repositories.ModulSnapshotRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ModulServiceTest {
    private ModulService modulService;


    private AntragRepository antragRepository;
    private ModulSnapshotRepository modulSnapshotRepository;

    private Modul modulforTagging;
    private Veranstaltung veranstaltungForTagging;

    private Modul modulSichtbarkeitTrue;
    private Modul modulSichtbarkeitFalse;
    private Modul modulSichtbarkeitNull;



    private String modul1;
    private String modul2;
    private String modul3;
    private String modul4;
    private String diffs1;
    private String diffs2;

    /**
     * Erstellt Beispiel-Objekte als Strings, aus denen anschließend JsonService Module erstellt.
     */
    @BeforeEach
    public void init() {
        antragRepository = mock(AntragRepository.class);
        modulSnapshotRepository = mock(ModulSnapshotRepository.class);
        modulService = new ModulService(antragRepository, modulSnapshotRepository);

        modulforTagging = ModulFaker.generateFakeModul();
        modulforTagging.setId(3301L);

        modulSichtbarkeitTrue = ModulFaker.generateFakeModul();
        modulSichtbarkeitTrue.setId(3302L);
        modulSichtbarkeitTrue.setSichtbar(true);
        modulSnapshotRepository.save(modulSichtbarkeitTrue);

        modulSichtbarkeitFalse = ModulFaker.generateFakeModul();
        modulSichtbarkeitFalse.setId(3303L);
        modulSichtbarkeitFalse.setSichtbar(false);
        modulSnapshotRepository.save(modulSichtbarkeitFalse);

        modulSichtbarkeitNull = ModulFaker.generateFakeModul();
        modulSichtbarkeitNull.setId(3304L);
        modulSichtbarkeitNull.setSichtbar(null);
        modulSnapshotRepository.save(modulSichtbarkeitNull);

        veranstaltungForTagging = modulforTagging
                .getVeranstaltungen()
                .stream()
                .findFirst()
                .orElse(null);
        veranstaltungForTagging.setId(3300L);

        modulSnapshotRepository.save(modulforTagging);

        modul1 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3}],"
                + "\"modulkategorie\":\"MASTERARBEIT\"}";
        modul2 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3}],"
                + "\"modulkategorie\":\"BACHELORARBEIT\"}";
        modul3 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3}],"
                + "\"modulkategorie\":\"MASTERARBEIT\"}";
        modul4 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3,"
                + "\"voraussetzungenTeilnahme\":\"Informatik I\"}],"
                + "\"modulkategorie\":\"BACHELORARBEIT\"}";
        diffs1 = "{\"id\":5,"
                + "\"modulkategorie\":\"BACHELORARBEIT\"}";
        diffs2 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3,"
                + "\"voraussetzungenTeilnahme\":\"Informatik I\"}],"
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

    @Test
    public void testKopiereModul() {
        Modul modulAlt = JsonService.jsonObjectToModul(modul1);
        Modul modulNeu = new Modul();
        modulService.copyModul(modulAlt,modulNeu);

        Modul diffs = modulService.calculateModulDiffs(modulAlt, modulNeu);
        assertThat(diffs).isNull();
    }

    @Test
    public void getWinterSemesterYearTest() {
        String actual = ModulService.getWinterSemesterYear(2019);
        assertThat(actual).isEqualTo("2019-20");
    }

    @Test
    public void getWinterSemesterFromDateTest() {
        LocalDateTime localDateTime = LocalDateTime.of(2020, 3, 22, 17, 6);
        String actual = ModulService.getSemesterFromDate(localDateTime);
        assertThat(actual).isEqualTo("WiSe2019-20");
    }

    @Test
    public void getSommerSemesterFromDateTest() {
        LocalDateTime localDateTime = LocalDateTime.of(2020, 6, 22, 17, 6);
        String actual = ModulService.getSemesterFromDate(localDateTime);
        assertThat(actual).isEqualTo("SoSe2020");
    }

    @Test
    public void getPastAndNextSemestersTest() {
        LocalDateTime localDateTime = LocalDateTime.of(2020, 3, 22, 17, 6);
        List<String> actual = ModulService.getPastAndNextSemesters(localDateTime,2, 3);
        List<String> expected = Arrays.asList("WiSe2018-19", "SoSe2019", "WiSe2019-20",
                "SoSe2020", "WiSe2020-21");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void addSemesterTag() {

        when(modulSnapshotRepository.findById(modulforTagging.getId())).thenReturn(
                Optional.ofNullable(modulforTagging)
        );

        modulService.tagVeranstaltungSemester(
                "SoSe1995",
                veranstaltungForTagging.getId(),
                modulforTagging.getId()
        );

        assertThat(veranstaltungForTagging.getSemester()).contains("SoSe1995");
    }

    @Test
    public void deleteSemesterTag() {

        veranstaltungForTagging.setSemester(Collections.singleton("SoSe1998"));
        when(modulSnapshotRepository.findById(modulforTagging.getId())).thenReturn(
                Optional.ofNullable(modulforTagging)
        );

        modulService.deleteTagVeranstaltungSemester(
                "SoSe1998",
                veranstaltungForTagging.getId(),
                modulforTagging.getId()
        );

        assertThat(veranstaltungForTagging.getSemester()).doesNotContain("SoSe1998");
    }

    @Test
    public void changeSichtbarkeitFromTrue() {

        when(modulSnapshotRepository.findById(modulSichtbarkeitTrue.getId())).thenReturn(
                Optional.ofNullable(modulSichtbarkeitTrue)
        );

        modulService.changeVisibility(3302L);

        assertThat(modulSichtbarkeitTrue.getSichtbar()).isEqualTo(false);
    }

    @Test
    public void changeSichtbarkeitFromFalse() {

        when(modulSnapshotRepository.findById(modulSichtbarkeitFalse.getId())).thenReturn(
                Optional.ofNullable(modulSichtbarkeitFalse)
        );

        modulService.changeVisibility(3303L);

        assertThat(modulSichtbarkeitFalse.getSichtbar()).isEqualTo(true);
    }

    @Test
    public void changeSichtbarkeitFromNull() {

        when(modulSnapshotRepository.findById(modulSichtbarkeitNull.getId())).thenReturn(
                Optional.ofNullable(modulSichtbarkeitNull)
        );

        modulService.changeVisibility(3304L);

        assertThat(modulSichtbarkeitNull.getSichtbar()).isEqualTo(true);
    }

    @Test
    public void getAllSichtbareModuleTest() {
        Modul sichtbar = ModulFaker.generateFakeModul();
        sichtbar.setSichtbar(true);
        Modul sichtbar2 = ModulFaker.generateFakeModul();
        sichtbar2.setSichtbar(true);
        Modul nichtSichtbar = ModulFaker.generateFakeModul();
        nichtSichtbar.setSichtbar(false);

        List<Modul> ModulList = new ArrayList<>();
        ModulList.add(sichtbar);
        ModulList.add(sichtbar2);
        ModulList.add(nichtSichtbar);

        when(modulService.getAllModule()).thenReturn(ModulList);
        List<Modul> result = modulService.getAllSichtbareModule();
        assertEquals(2, result.size());
    }

}

