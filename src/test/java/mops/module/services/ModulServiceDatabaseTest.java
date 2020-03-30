package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import mops.module.generator.ModulFaker;
import mops.module.repositories.AntragRepository;
import mops.module.repositories.ModulSnapshotRepository;
import org.assertj.core.util.Lists;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ModulServiceDatabaseTest {
    private ModulService modulService;
    private AntragService antragService;

    @Autowired
    private AntragRepository antragRepository;

    @Autowired
    private ModulSnapshotRepository modulSnapshotRepository;

    private String modul1;
    private String modul2;
    private String modul3;
    private String modul4;
    private String diffs1;
    private String diffs2;
    private String completeModul;

    private CustomComparator ignoreDates;

    /**
     * Erstellt Beispiel-Objekte als Strings, aus denen anschließend JsonService Module erstellt.
     */
    @BeforeEach
    public void init() {
        modulService = new ModulService(antragRepository, modulSnapshotRepository);
        antragService = new AntragService(antragRepository, modulSnapshotRepository);

        // Ignore dates for JSON comparison
        ignoreDates = new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("datumErstellung", (o1, o2) -> true),
                new Customization("datumAenderung", (o1, o2) -> true));

        antragRepository.deleteAll();
        modulSnapshotRepository.deleteAll();

        modul1 = "{'veranstaltungen':[{'leistungspunkte':'5CP'}],"
                + "'modulkategorie':'MASTERARBEIT'}";
        modul2 = "{'veranstaltungen':[{'leistungspunkte':'5CP'}],"
                + "'modulkategorie':'BACHELORARBEIT'}";
        modul3 = "{'veranstaltungen':[{'leistungspunkte':'5CP',"
                + "'beschreibung':{'inhalte':'Lorem ipsum'}}],"
                + "'modulkategorie':'MASTERARBEIT'}";
        modul4 = "{'veranstaltungen':[{'leistungspunkte':'5CP',"
                + "'beschreibung':{'inhalte':'Lorem ipsum'}}],"
                + "'modulkategorie':'BACHELORARBEIT'}";
        diffs1 = "{'modulkategorie':'BACHELORARBEIT'}";
        diffs2 = "{'veranstaltungen':[{'id':3,"
                + "'voraussetzungenTeilnahme':[{'titel':'test'}]}],"
                + "'modulkategorie':'BACHELORARBEIT'}";
        completeModul = "{'titelDeutsch':'Betriebssysteme','titelEnglisch':'Operating systems',"
                + "'veranstaltungen':[{'titel':'Vorlesung Betriebssysteme','leistungspunkte':'10CP'"
                + ",'veranstaltungsformen':[{'form':'Vorlesung','semesterWochenStunden':4},"
                + "{'form':'Übung','semesterWochenStunden':2}],"
                + "'beschreibung':{'inhalte':'Inhalte','lernergebnisse':'Synchronisierung',"
                + "'literatur':'Alter Schinken','verwendbarkeit':'Überall verwendbar',"
                + "'voraussetzungenBestehen':'50% der Punkte in der Klausur',"
                + "'haeufigkeit':'Alle 2 Semester','sprache':'Deutsch'},"
                + "'voraussetzungenTeilnahme':'Informatik I',"
                + "'zusatzfelder':[{'titel':'Zusatzfeld2',"
                + "'inhalt':'Dies hier ist das zweite Zusatzfeld!'},"
                + "{'titel':'Zusatzfeld1','inhalt':'Dies hier ist das erste Zusatzfeld!'}]}],"
                + "'modulbeauftragte':'Michael Schöttner','gesamtLeistungspunkte':'10CP',"
                + "'studiengang':'Informatik','modulkategorie':'WAHLPFLICHT_BA'}";
    }

    @Test
    public void addModulCreationAntragTestCreateOne() {
        antragService.addModulCreationAntrag(JsonService.jsonObjectToModul(modul1),
                "Beispielantragsteller");
        assertThat(antragRepository.count()).isEqualTo(1);
    }

    @Test
    public void approveModulCreationAntragTestAntragCreated() {
        Antrag antrag = antragService.addModulCreationAntrag(JsonService.jsonObjectToModul(modul1),
                "Beispielantragsteller");
        antragService.approveModulCreationAntrag(antrag);
        assertThat(modulSnapshotRepository.count()).isEqualTo(1);
    }

    @Test
    public void approveModulCreationAntragTestModulIdIsAddedToAntrag() {
        Antrag antrag = antragService.addModulCreationAntrag(JsonService.jsonObjectToModul(modul1),
                "Beispielantragsteller");
        Modul modul = antragService.approveModulCreationAntrag(antrag);
        assertThat(antrag.getModulId()).isEqualTo(modul.getId());
    }

    @Test
    public void approveModulCreationAntragTestFullRoutine() {
        Modul vergleichsmodul = new Modul();
        vergleichsmodul.setModulkategorie(Modulkategorie.MASTERARBEIT);
        Veranstaltung veranstaltung = new Veranstaltung();
        veranstaltung.setLeistungspunkte("5CP");
        Veranstaltungsbeschreibung veranstaltungsbeschreibung = new Veranstaltungsbeschreibung();
        veranstaltungsbeschreibung.setInhalte("Hier sind Inhalte");
        veranstaltung.setBeschreibung(veranstaltungsbeschreibung);
        vergleichsmodul.addVeranstaltung(veranstaltung);

        Antrag antrag = antragService.addModulCreationAntrag(vergleichsmodul,
                "Beispielantragsteller");
        Modul modul = antragService.approveModulCreationAntrag(antrag);
        vergleichsmodul.setId(modul.getId());

        try {
            JSONAssert.assertEquals(JsonService.modulToJsonObject(vergleichsmodul),
                    JsonService.modulToJsonObject(modul), ignoreDates);
        } catch (JSONException e) {
            fail(e.toString());
        }
    }

    @Test
    public void approveModulModificationAntragTest() {
        Antrag creationAntrag = antragService.addModulCreationAntrag(
                JsonService.jsonObjectToModul(modul3), "Beispielantragsteller");
        Modul modul = antragService.approveModulCreationAntrag(creationAntrag);

        Modul aenderungen = JsonService.jsonObjectToModul(modul4);
        aenderungen.setId(modul.getId());

        Antrag modificationAntrag = antragService.addModulModificationAntrag(aenderungen,
                "Beispielantragsteller");
        Modul geaendertesModul = antragService.approveModulModificationAntrag(modificationAntrag);

        try {
            JSONAssert.assertEquals(JsonService.modulToJsonObject(aenderungen),
                    JsonService.modulToJsonObject(geaendertesModul), ignoreDates);
        } catch (JSONException e) {
            fail(e.toString());
        }

    }

    @Test
    public void completeAddModulRoutineTest() {

        Modul modul = JsonService.jsonObjectToModul(completeModul);

        modul.refreshMapping();
        Antrag creationAntrag = antragService.addModulCreationAntrag(modul,
                "Beispielantragsteller");

        Modul dbmodul = antragService.approveModulCreationAntrag(creationAntrag);

        dbmodul.setModulbeauftragte("Stefan Harmeling");

        Antrag modificationAntrag = antragService.addModulModificationAntrag(dbmodul,
                "Beispielantragsteller");
        Modul modifiedModul = antragService.approveModulModificationAntrag(modificationAntrag);

        assertThat(modifiedModul.getModulbeauftragte()).isEqualTo("Stefan Harmeling");
    }

    @Test
    public void semesterQueryTest() {
        Modul modul1 = JsonService.jsonObjectToModul(completeModul);
        Modul modul2 = JsonService.jsonObjectToModul(completeModul);

        modul1.getVeranstaltungen().stream().findFirst().orElse(null)
                .setSemester(new HashSet<>(Arrays.asList("WiSe2018-19", "SoSe2019")));
        modul2.getVeranstaltungen().stream().findFirst().orElse(null)
                .setSemester(new HashSet<>(Arrays.asList("WiSe2019-20")));

        Antrag antrag1 = antragService.addModulCreationAntrag(modul1, "Beispielantragsteller");
        Antrag antrag2 = antragService.addModulCreationAntrag(modul2, "Beispielantragsteller");

        antragService.approveModulCreationAntrag(antrag1);
        antragService.approveModulCreationAntrag(antrag2);

        List<Modul> dbmodule = modulSnapshotRepository.findModuleBySemester("WiSe2018-19");

        try {
            assertThat(dbmodule.size()).isEqualTo(1);
            JSONAssert.assertEquals(JsonService.modulToJsonObject(modul1),
                    JsonService.modulToJsonObject(dbmodule.get(0)), ignoreDates);
        } catch (JSONException e) {
            fail(e.toString());
        }
    }

    @Test
    public void testgetAlleAntrage() {
        Modul modul1 = ModulFaker.generateFakeModul();
        Modul modul2 = ModulFaker.generateFakeModul();
        Modul modul3 = ModulFaker.generateFakeModul();

        antragService.addModulCreationAntrag(modul1, "Beispielantragsteller1");
        antragService.addModulCreationAntrag(modul2, "Beispielantragsteller2");
        antragService.addModulCreationAntrag(modul3, "Beispielantragsteller3");

        List<Antrag> antraege = antragService.getAlleAntraege();

        assertThat(antraege.size()).isEqualTo(3);
    }

    @Test
    public void testgetAlleAntraegeGeordnetDatum() {

        Modul modul1 = ModulFaker.generateFakeModul();
        Modul modul2 = ModulFaker.generateFakeModul();
        Modul modul3 = ModulFaker.generateFakeModul();

        antragService.addModulCreationAntrag(modul1, "Beispielantragsteller1");
        antragService.addModulCreationAntrag(modul2, "Beispielantragsteller2");
        antragService.addModulCreationAntrag(modul3, "Beispielantragsteller3");

        //Sortierung von Alt nach Neu.
        List<Antrag> antraege = antragService.getAlleAntraegeGeordnetDatum();

        assertEquals(JsonService.modulToJsonObject(modul3),
                antraege.get(0).getJsonModulAenderung());
    }

    @Test
    public void testgetAlleOffenenAntraegeGeordnetDatumAnzahl() {

        Modul modul1 = ModulFaker.generateFakeModul();
        Modul modul2 = ModulFaker.generateFakeModul();
        Modul modul3 = ModulFaker.generateFakeModul();

        antragService.addModulCreationAntrag(modul1, "Beispielantragsteller1");
        antragService.addModulCreationAntrag(modul2, "Beispielantragsteller2");
        Antrag antrag3 = antragService.addModulCreationAntrag(modul3, "Beispielantragsteller3");

        antragService.approveModulCreationAntrag(antrag3);

        List<Antrag> antraege = antragService.getAlleOffenenAntraegeGeordnetDatum();

        assertThat(antragService.getAlleAntraege().size()).isEqualTo(3);
        assertThat(antraege.size()).isEqualTo(2);
        assertEquals(JsonService.modulToJsonObject(modul2),
                antraege.get(0).getJsonModulAenderung());

    }

    @Test
    void getAllVersionsOfModulOldestFirstTestThrowsExceptionWhenInvalidId() {
        modulSnapshotRepository.deleteAll();
        antragRepository.deleteAll();
        Modul modul = ModulFaker.generateFakeModul();
        modul.setId(3301L);

        assertThrows(Exception.class,
                () -> antragService.getAllVersionsOfModulOldestFirst(modul.getId()));
    }

    @Test
    void getAllVersionsOfModulOldestFirstTestThrowsExceptionWhenAntragMissing() {
        modulSnapshotRepository.deleteAll();
        antragRepository.deleteAll();
        Modul modul = modulSnapshotRepository.save(ModulFaker.generateFakeModul());

        assertThrows(Exception.class,
                () -> antragService.getAllVersionsOfModulOldestFirst(modul.getId()));
    }

    @Test
    void getAllVersionsOfModulOldestFirstTestWhenNoPreviousVersions() {
        Modul modul1 = ModulFaker.generateFakeModul();
        modul1.setTitelDeutsch("initialer Titel");
        Antrag antrag1 = antragService.addModulCreationAntrag(modul1, "Beispielantragsteller1");
        Modul modul2 = antragService.approveModulCreationAntrag(antrag1);

        LinkedList<Modul> versionen =
                antragService.getAllVersionsOfModulOldestFirst(modul2.getId());

        assertThat(versionen.get(0).getTitelDeutsch()).isEqualTo("initialer Titel");
    }

    @Test
    void getAllVersionsOfModulOldestFirstTestModulTitel() {
        Modul modul1 = ModulFaker.generateFakeModul();
        modul1.setTitelDeutsch("initialer Titel");
        Antrag antrag1 = antragService.addModulCreationAntrag(modul1, "Antragsteller1");
        Modul modul2 = antragService.approveModulCreationAntrag(antrag1);

        modul2.setTitelDeutsch("zweiter Titel");
        Antrag antrag2 = antragService.addModulModificationAntrag(modul2, "Antragsteller2");
        Modul modul3 = antragService.approveModulModificationAntrag(antrag2);

        modul3.setTitelDeutsch("dritter Titel");
        Antrag antrag3 = antragService.addModulModificationAntrag(modul3, "Antragsteller3");
        Modul modul4 = antragService.approveModulModificationAntrag(antrag3);

        LinkedList<Modul> versionen =
                antragService.getAllVersionsOfModulOldestFirst(modul4.getId());

        ArrayList<String> actualTitles = Lists.newArrayList(
                versionen.get(0).getTitelDeutsch(),
                versionen.get(1).getTitelDeutsch(),
                versionen.get(2).getTitelDeutsch());
        ArrayList<String> expectedTitles = Lists.newArrayList(
                "initialer Titel", "zweiter Titel", "dritter Titel");

        MatcherAssert.assertThat(actualTitles, Matchers.equalTo(expectedTitles));
    }

    @Test
    void getAllVersionsOfModulOldestFirstTestVeranstaltungTitel() {
        Modul modul1 = ModulFaker.generateFakeModul();
        Veranstaltung veranstaltungInModul = modul1.getVeranstaltungen()
                .stream().findFirst().orElse(null);
        modul1.getVeranstaltungen().removeIf(v -> v != veranstaltungInModul);

        modul1.getVeranstaltungen().stream().findFirst().orElse(null).setTitel("Titel 1");
        Antrag antrag1 = antragService.addModulCreationAntrag(modul1, "Antragsteller1");
        Modul modul2 = antragService.approveModulCreationAntrag(antrag1);

        modul2.getVeranstaltungen().stream().findFirst().orElse(null).setTitel("Titel 2");
        Antrag antrag2 = antragService.addModulModificationAntrag(modul2, "Antragsteller2");
        Modul modul3 = antragService.approveModulModificationAntrag(antrag2);

        modul3.getVeranstaltungen().stream().findFirst().orElse(null).setTitel("Titel 3");
        Antrag antrag3 = antragService.addModulModificationAntrag(modul3, "Antragsteller3");
        Modul modul4 = antragService.approveModulModificationAntrag(antrag3);

        LinkedList<Modul> versionen = antragService.getAllVersionsOfModulOldestFirst(
                modul4.getId());

        ArrayList<String> actualTitles = Lists.newArrayList(
                versionen.get(0).getVeranstaltungen()
                        .stream().findFirst().orElse(null).getTitel(),
                versionen.get(1).getVeranstaltungen()
                        .stream().findFirst().orElse(null).getTitel(),
                versionen.get(2).getVeranstaltungen()
                        .stream().findFirst().orElse(null).getTitel());
        ArrayList<String> expectedTitles = Lists.newArrayList(
                "Titel 1", "Titel 2", "Titel 3");

        MatcherAssert.assertThat(actualTitles, Matchers.equalTo(expectedTitles));
    }

}
