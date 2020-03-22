package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import mops.module.database.Modul;
import mops.module.repositories.ModulSnapshotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FullTextSearchTest {

    @Autowired
    SuchService suchService;

    @Autowired
    ModulSnapshotRepository modulRepo;

    private String completeModulString;
    private Modul completeModul;

    @BeforeEach
    void init() {
        modulRepo.deleteAll();
        completeModulString = "{'titelDeutsch':'Betriebssysteme','titelEnglisch':'Operating systems',"
                + "'veranstaltungen':[{'titel':'Vorlesung Betriebssysteme','leistungspunkte':'10CP'"
                + ",'veranstaltungsformen':[{'form':'Vorlesung','semesterWochenStunden':4},"
                + "{'form':'Übung','semesterWochenStunden':2}],"
                + "'beschreibung':{'inhalte':' \uF0B7Architekturformen: monolitisch, geschichtet, Mikrokern, Client/Server\uF0B7Speicher: Segmentierung, Paging, Garbage Collection\uF0B7Nebenläufigkeit: Schedulingstrategien, Prozesse, Threads, Interrupts\uF0B7Synchronisierung: Semaphore, klassische Problemstellungen, Verklemmungen\uF0B7Dateisysteme: FAT, UNIX, EXT, NTFS\uF0B7Kommunikation: Signale, Pipes, Sockets\uF0B7Sicherheit: HW-Schutz\uF0B7Fallstudien, u.a. Linux, Microsoft Windows',"
                + "'lernergebnisse':'Studierende sollen nach Absolvierung der Lehrveranstaltungen in der Lage sein,\uF0B7Betriebssystembegriffe zu nennen und zu erläutern\uF0B7Speicherverwaltungstechniken (physikalisch, virtuell, Segmentierung und Paging) auf gegebene Bei-spiele anzuwenden und zu bewerten.\uF0B7Schedulingstrategien anzuwenden und zu bewerten.\uF0B7Synchronisierungsprobleme in parallelen Threads zu erkennen und eigene Synchronisierungslösungen zu konzipieren\uF0B7Interprozesskommunikation anzuwenden\uF0B7grundlegende Betriebssystemkonzepte in modernen Desktop-Betriebssystemen in eigenen Worten erklären zu können',"
                + "'literatur':['\uF0B7Andrew S. Tanenbaum: „Modern Operating Systems”, 4. Auflage, Prentice Hall, 2014.'],'verwendbarkeit':['Wahlpflichtbereich','Schwerpunktbereich','Individuelle Ergänzung im Master-Studiengang Informatik','Anwendungsfach im Bachelor-Studiengang Mathematik und Anwendungsgebiete','Nebenfach im Bachelor-Studiengang Physik','Nebenfach im Bachelor-Studiengang Medizinische Physik'],"
                + "'voraussetzungenBestehen':['Erfolgreiche Teilnahme an der Prüfung am Ende der Veranstaltung.'],"
                + "'haeufigkeit':'Alle 2 Semester','sprache':'Deutsch'},"
                + "'voraussetzungenTeilnahme':['„Programmierung”','„Rechnerarchitektur”'],"
                + "'zusatzfelder':[{'titel':'Zusatzfeld2',"
                + "'inhalt':'Dies hier ist das zweite Zusatzfeld!'},"
                + "{'titel':'Zusatzfeld1','inhalt':'Dies hier ist das erste Zusatzfeld!'}]}],"
                + "'modulbeauftragte':['Michael Schöttner'],'gesamtLeistungspunkte':'10CP',"
                + "'studiengang':'Informatik','modulkategorie':'WAHLPFLICHT_BA'}";

        completeModul = JsonService.jsonObjectToModul(completeModulString);
        completeModul.refreshMapping();
        modulRepo.save(completeModul);
    }

    @Test
    void fullTextSearchMultiWordTest() {
        List<Modul> results = suchService.searchInVeranstaltungsbeschreibung("geschichtet, problemstellung");

        assertFalse(results.isEmpty());
        assertThat(results.get(0).getTitelDeutsch().equals(completeModul.getTitelDeutsch()));
    }

    @Test
    @DisplayName("Search for 'Architektur' finds 'Architekturformen'")
    void fullTextSearchStemWordTest() {
        List<Modul> results = suchService.searchInVeranstaltungsbeschreibung("Architektur");

        assertFalse(results.isEmpty());
        assertThat(results.get(0).getTitelDeutsch().equals(completeModul.getTitelDeutsch()));
    }
}
