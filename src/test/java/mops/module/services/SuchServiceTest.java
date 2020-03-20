package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import mops.module.database.Modul;
import mops.module.repositories.ModulSnapshotRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@ActiveProfiles("dev")
public class SuchServiceTest {

    @Autowired
    SuchService suchService;

    @Autowired
    ModulSnapshotRepository modulRepo;

    Modul modul;
    Modul modul2;

    @BeforeEach
    public void init() {

        modul = new Modul();
        modul.setTitelDeutsch("Programmierung");
        modul.setTitelEnglisch("Programming 101");

        modul2 = new Modul();
        modul2.setTitelDeutsch("Graphen, Algorithmen und Entscheidungsprobleme");
        modul2.setTitelEnglisch("Graphs, algorithms and decision-making problems");

        modulRepo.save(modul);
        modulRepo.save(modul2);
    }

    @AfterEach
    public void cleanUp() {
        modulRepo.deleteAll();
    }

    @Test
    void searchReturnsCorrectResultSize() {
        List<Modul> results = suchService.searchForModuleByTitle(modul.getTitelDeutsch());
        assertTrue(results.size() == 1);
    }

    @Test
    void returnedModulContainsSearchterm() {
        List<Modul> results = suchService.searchForModuleByTitle(modul.getTitelDeutsch());
        assertTrue(results.get(0).getTitelDeutsch().equals(modul.getTitelDeutsch()));
    }

    @Test
    void findWordsAlthoughTheyAreInUpperOrLowercase() {
        List<Modul> results = suchService.searchForModuleByTitle("programmierung");
        assertTrue(results.get(0).getTitelDeutsch().equals(modul.getTitelDeutsch()));
    }

    @Test
    void searchForSubstringReturnsResult() {
        List<Modul> results = suchService.searchForModuleByTitle("prog");
        assertTrue(results.size() == 1);
        assertTrue(results.get(0).getTitelDeutsch().equals(modul.getTitelDeutsch()));
    }

    @Test
    void unsuccessfulSearchReturnsEmptyList() {
        List<Modul> results = suchService.searchForModuleByTitle("katze");
        assertTrue(results.isEmpty());
    }

    @Test
    void fullTextSearchTest() {
        String completeModul = "{'titelDeutsch':'Betriebssysteme','titelEnglisch':'Operating systems',"
                + "'veranstaltungen':[{'titel':'Vorlesung Betriebssysteme','leistungspunkte':'10CP'"
                + ",'veranstaltungsformen':[{'form':'Vorlesung','semesterWochenStunden':4},"
                + "{'form':'Übung','semesterWochenStunden':2}],"
                + "'beschreibung':{'inhalte':' monolitisch, geschichtet, Mikrokern, Client Server Semaphore, klassische Problemstellungen, Verklemmungen',"
                + "'lernergebnisse':'Synchronisierung',"
                + "'literatur':['Alter Schinken'],'verwendbarkeit':['Überall verwendbar'],"
                + "'voraussetzungenBestehen':['50% der Punkte in der Klausur'],"
                + "'haeufigkeit':'Alle 2 Semester','sprache':'Deutsch'},"
                + "'voraussetzungenTeilnahme':['Informatik I'],"
                + "'zusatzfelder':[{'titel':'Zusatzfeld2',"
                + "'inhalt':'Dies hier ist das zweite Zusatzfeld!'},"
                + "{'titel':'Zusatzfeld1','inhalt':'Dies hier ist das erste Zusatzfeld!'}]}],"
                + "'modulbeauftragte':['Michael Schöttner'],'gesamtLeistungspunkte':'10CP',"
                + "'studiengang':'Informatik','modulkategorie':'WAHLPFLICHT_BA'}";

        Modul complete = JsonService.jsonObjectToModul(completeModul);
        complete.refreshMapping();
        modulRepo.save(complete);
        List<Modul> results = suchService.searchInVeranstaltungsbeschreibung("problemstellung");

        assertTrue(!results.isEmpty());
        assertThat(results.get(0).getTitelDeutsch().equals(complete.getTitelDeutsch()));

    }
}