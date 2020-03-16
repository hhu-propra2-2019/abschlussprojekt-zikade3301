package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.database.Modulkategorie;
import mops.module.database.Veranstaltung;
import mops.module.database.Veranstaltungsbeschreibung;
import mops.module.database.Zusatzfeld;
import mops.module.repositories.AntragsRepository;
import mops.module.repositories.ModulSnapshotRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
//@ActiveProfiles("dev")
public class ModulServiceDatabaseTest {
    private ModulService modulService;

    @Autowired
    private AntragsRepository antragsRepository;

    @Autowired
    private ModulSnapshotRepository modulSnapshotRepository;

    private String modul1;
    private String modul2;
    private String modul3;
    private String modul4;
    private String diffs1;
    private String diffs2;

    @BeforeEach
    public void init() {
        modulService = new ModulService(antragsRepository, modulSnapshotRepository);

        antragsRepository.deleteAll();
        modulSnapshotRepository.deleteAll();

        modul1 = "{\"veranstaltungen\":[{\"creditPoints\":\"5CP\"}],"
                + "\"modulkategorie\":\"MASTERARBEIT\"}";
        modul2 = "{\"veranstaltungen\":[{\"creditPoints\":\"5CP\"}],"
                + "\"modulkategorie\":\"BACHELORARBEIT\"}";
        modul3 = "{\"veranstaltungen\":[{\"creditPoints\":\"5CP\","
                + "\"beschreibung\":{\"inhalte\":\"Lorem ipsum\"}}],"
                + "\"modulkategorie\":\"MASTERARBEIT\"}";
        modul4 = "{\"veranstaltungen\":[{\"creditPoints\":\"5CP\","
                + "\"beschreibung\":{\"inhalte\":\"Lorem ipsum\"}}],"
                + "\"modulkategorie\":\"BACHELORARBEIT\"}";
        diffs1 = "{\"modulkategorie\":\"BACHELORARBEIT\"}";
        diffs2 = "{\"veranstaltungen\":[{\"id\":3,"
                + "\"voraussetzungenTeilnahme\":[{\"titel\":\"test\"}]}],"
                + "\"modulkategorie\":\"BACHELORARBEIT\"}";
    }

    @Test
    public void addModulCreationAntragTestCreateOne() {
        modulService.addModulCreationAntrag(JsonService.jsonObjectToModul(modul1));
        assertThat(antragsRepository.count()).isEqualTo(1);
    }

    /*@Test
    public void addModulModificationAntragTestCreateOne() {
        Modul testmodul = jsonService.jsonObjectToModul(modul1);
        testmodul.setId(20L);
        modulService.addModulModificationAntrag(testmodul);
        assertThat(antragsRepository.count()).isEqualTo(1);
    }*/

    @Test
    public void approveModulCreationAntragTestAntragCreated() {
        modulService.addModulCreationAntrag(JsonService.jsonObjectToModul(modul1));
        Antrag antrag = modulService.getAlleAntraege().get(0);
        modulService.approveModulCreationAntrag(antrag);
        assertThat(modulSnapshotRepository.count()).isEqualTo(1);
    }

    @Test
    public void approveModulCreationAntragTestModulIdIsAddedToAntrag() {
        modulService.addModulCreationAntrag(JsonService.jsonObjectToModul(modul1));
        Antrag antrag = modulService.getAlleAntraege().get(0);
        modulService.approveModulCreationAntrag(antrag);
        Modul modul = modulService.getAlleModule().get(0);
        assertThat(antrag.getModulid()).isEqualTo(modul.getId());
    }

    @Test
    public void approveModulCreationAntragTestFullRoutine() {
        Modul vergleichsmodul = new Modul();
        vergleichsmodul.setModulkategorie(Modulkategorie.MASTERARBEIT);
        Veranstaltung veranstaltung = new Veranstaltung();
        veranstaltung.setCreditPoints("5CP");
        Veranstaltungsbeschreibung veranstaltungsbeschreibung = new Veranstaltungsbeschreibung();
        veranstaltungsbeschreibung.setInhalte("Hier sind Inhalte");
        veranstaltung.setBeschreibung(veranstaltungsbeschreibung);
        vergleichsmodul.addVeranstaltung(veranstaltung);

        modulService.addModulCreationAntrag(vergleichsmodul);
        List<Antrag> antraege = modulService.getAlleAntraege();
        modulService.approveModulCreationAntrag(antraege.get(antraege.size() - 1));

        List<Modul> module = modulService.getAlleModule();
        Modul modul = module.get(module.size() - 1);
        vergleichsmodul.setId(modul.getId());

        try {
            JSONAssert.assertEquals(JsonService.modulToJsonObject(vergleichsmodul),
                    JsonService.modulToJsonObject(modul), false);
        } catch (JSONException e) {
            fail(e.toString());
        }
    }

    @Test
    public void approveModulModificationAntragTest() {
        modulService.addModulCreationAntrag(JsonService.jsonObjectToModul(modul3));

        List<Antrag> antraege = modulService.getAlleAntraege();
        modulService.approveModulCreationAntrag(antraege.get(antraege.size() - 1));

        List<Modul> module = modulService.getAlleModule();
        Modul modul = module.get(module.size() - 1);

        Modul aenderungen = JsonService.jsonObjectToModul(modul4);

        aenderungen.setId(modul.getId());
        modulService.addModulModificationAntrag(aenderungen);

        antraege = modulService.getAlleAntraege();
        Antrag antrag = antraege.get(antraege.size() - 1);

        modulService.approveModulModificationAntrag(antrag);

        module = modulService.getAlleModule();
        Modul geaendertesModul = module.get(module.size() - 1);

        Modul assertmodul = JsonService.jsonObjectToModul(modul4);
        assertmodul.setId(geaendertesModul.getId());

        try {
            JSONAssert.assertEquals(JsonService.modulToJsonObject(assertmodul),
                    JsonService.modulToJsonObject(geaendertesModul), false);
        } catch (JSONException e) {
            fail(e.toString());
        }

    }

    @Test
    public void completeAddModulRoutineTest() {
        Modul modul = new Modul();
        modul.setGesamtCreditPoints("10CP");
        modul.setModulkategorie(Modulkategorie.WAHLPFLICHT_BA);
        modul.setStudiengang("Informatik");
        modul.setTitelDeutsch("Betriebssysteme");
        modul.setTitelEnglisch("Operating systems");

        Veranstaltung veranstaltung = new Veranstaltung();
        veranstaltung.setCreditPoints("10CP");
        veranstaltung.setTitel("Vorlesung Betriebssysteme");

        Veranstaltungsbeschreibung veranstaltungsbeschreibung = new Veranstaltungsbeschreibung();
        veranstaltungsbeschreibung.setInhalte("Inhalte");
        veranstaltungsbeschreibung.setHaufigkeit("Alle 2 Semester");
        veranstaltungsbeschreibung.setLernergebnisse("Synchronisierung");
        veranstaltungsbeschreibung.setSprache("Deutsch");
        veranstaltungsbeschreibung.setLiteratur(
                new HashSet<>(Arrays.asList("Alter Schinken")));
        veranstaltungsbeschreibung.setVerwendbarkeit(
                new HashSet<>(Arrays.asList("Überall verwendbar")));
        veranstaltungsbeschreibung.setVoraussetzungenBestehen(
                new HashSet<>(Arrays.asList("50% der Punkte in der Klausur")));
        veranstaltung.setBeschreibung(veranstaltungsbeschreibung);

        veranstaltung.setLehrende(
                new HashSet<>(Arrays.asList("Michael Schöttner")));
        veranstaltung.setVeranstaltungsformen(
                new HashSet<>(Arrays.asList("Vorlesung", "Praktische Übung")));

        veranstaltung.setVoraussetzungenTeilnahme(
                new HashSet<>(Arrays.asList("Informatik I")));

        modul.setVeranstaltungen(
                new HashSet<>(Arrays.asList(veranstaltung)));

        Zusatzfeld feld1 = new Zusatzfeld();
        feld1.setInhalt("Dies hier ist das erste Zusatzfeld!");
        feld1.setTitel("Feld1");

        Zusatzfeld feld2 = new Zusatzfeld();
        feld2.setInhalt("Numero dos");
        feld2.setTitel("Feld2");

        modul.setZusatzfelder(new HashSet<>(Arrays.asList(feld1, feld2)));

        System.out.println(JsonService.modulToJsonObject(modul));

        modulService.addModulCreationAntrag(modul);

        List<Antrag> antraege = modulService.getAlleAntraege();
        modulService.approveModulCreationAntrag(antraege.get(antraege.size() - 1));

        List<Modul> module = modulService.getAlleModule();
        Modul dbmodul = module.get(module.size() - 1);

        for(Veranstaltung v : dbmodul.getVeranstaltungen()) {
            v.setLehrende(new HashSet<>(Arrays.asList("Stefan Harmeling")));
        }

        modulService.addModulModificationAntrag(dbmodul);

        antraege = modulService.getAlleAntraege();
        modulService.approveModulModificationAntrag(antraege.get(antraege.size() - 1));
    }

}
