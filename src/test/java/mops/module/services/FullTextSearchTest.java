package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import mops.module.database.Modul;
import mops.module.repositories.ModulSnapshotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FullTextSearchTest {

    @Autowired
    SuchService suchService;

    @Autowired
    ModulSnapshotRepository modulRepo;

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
        List<Modul> results = suchService.searchInVeranstaltungsbeschreibung("geschichtet, problemstellung");

        assertFalse(results.isEmpty());
        assertThat(results.get(0).getTitelDeutsch().equals(complete.getTitelDeutsch()));

    }
}
