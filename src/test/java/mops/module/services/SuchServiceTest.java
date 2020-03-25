package mops.module.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.repositories.ModulSnapshotRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class SuchServiceTest {

    @Autowired
    private SuchService suchService;

    @Autowired
    private ModulSnapshotRepository modulRepo;

    private Modul completeModul;
    private Modul anotherModul;

    @BeforeEach
    void init() {
        modulRepo.deleteAll();
        String completeModulString = "{'titelDeutsch':'Betriebssysteme',"
                + "'titelEnglisch':'Operating systems', 'veranstaltungen':"
                + "[{'titel':'Vorlesung Betriebssysteme','leistungspunkte':'10CP',"
                + "'veranstaltungsformen':[{'form':'Vorlesung','semesterWochenStunden':4},"
                + "{'form':'Übung','semesterWochenStunden':2}],"
                + "'beschreibung':{'inhalte':'"
                + "*Architekturformen: monolitisch, geschichtet, Mikrokern, Client/Server"
                + "*Speicher: Segmentierung, Paging, Garbage Collection"
                + "*Nebenläufigkeit: Schedulingstrategien, Prozesse, Threads, Interrupts"
                + "*Synchronisierung: Semaphore, klassische Problemstellungen, Verklemmungen"
                + "*Dateisysteme: FAT, UNIX, EXT, NTFS"
                + "*Kommunikation: Signale, Pipes, Sockets"
                + "*Sicherheit: HW-Schutz"
                + "*Fallstudien, u.a. Linux, Microsoft Windows',"
                + "'lernergebnisse':'Studierende sollen nach Absolvierung der Lehrveranstaltungen "
                + "in der Lage sein,*Betriebssystembegriffe zu nennen und zu erläutern"
                + "*Speicherverwaltungstechniken (physikalisch, virtuell, Segmentierung und "
                + "Paging) auf gegebene Bei-spiele anzuwenden und zu bewerten."
                + "*Schedulingstrategien anzuwenden und zu bewerten."
                + "*Synchronisierungsprobleme in parallelen Threads zu erkennen und eigene"
                + "Synchronisierungslösungen zu konzipieren"
                + "*Interprozesskommunikation anzuwenden"
                + "*grundlegende Betriebssystemkonzepte in modernen Desktop-Betriebssystemen"
                + "in eigenen Worten erklären zu können',"
                + "'literatur':['Andrew S. Tanenbaum: „Modern Operating Systems”, 4. Auflage,"
                + "Prentice Hall, 2014.'],'verwendbarkeit':['Wahlpflichtbereich',"
                + "'Schwerpunktbereich','Individuelle Ergänzung im Master-Studiengang Informatik',"
                + "'Anwendungsfach im Bachelor-Studiengang Mathematik und Anwendungsgebiete',"
                + "'Nebenfach im Bachelor-Studiengang Physik','Nebenfach im Bachelor-Studiengang "
                + "Medizinische Physik'], 'voraussetzungenBestehen':['Erfolgreiche Teilnahme an der"
                + "Prüfung am Ende der Veranstaltung.'], 'haeufigkeit':'Alle 2 Semester',"
                + "'sprache':'Deutsch'}, 'voraussetzungenTeilnahme':['„Programmierung”',"
                + "'„Rechnerarchitektur”'], 'zusatzfelder':[{'titel':'Zusatzfeld2', 'inhalt':"
                + "'Dies hier ist das zweite Zusatzfeld!'},"
                + "{'titel':'Zusatzfeld1','inhalt':'Dies hier ist das erste Zusatzfeld!'}]}],"
                + "'modulbeauftragte':['Michael Schöttner'],'gesamtLeistungspunkte':'10CP',"
                + "'studiengang':'Informatik','modulkategorie':'WAHLPFLICHT_BA','sichtbar':'true'}";

        completeModul = JsonService.jsonObjectToModul(completeModulString);
        completeModul.refreshMapping();
        modulRepo.save(completeModul);

        String anotherModulString = "{'titelDeutsch':'Programmierung',"
                + "'titelEnglisch':'Programming', 'veranstaltungen':[{'titel':"
                + "'Vorlesung Programmierung','leistungspunkte':'10CP'}],'sichtbar':'true'}";

        anotherModul = JsonService.jsonObjectToModul(anotherModulString);
        anotherModul.refreshMapping();
        modulRepo.save(anotherModul);
    }

    @AfterEach
    void cleanUp() {
        modulRepo.deleteAll();
    }

    @Test
    void fullTextSearchReturnsNoResultWhenNoMatch() {
        String searchinput = "Internet";
        List<Modul> results = suchService.search(searchinput);

        assertTrue(results.isEmpty());
    }

    @Test
    void fullTextSearchReturnsOneResultWhenOneMatch() {
        String searchinput = "Betriebssysteme";
        List<Modul> results = suchService.search(searchinput);

        assertEquals(1, results.size());
    }

    @Test
    void fullTextSearchReturnsTwoResultsWhenTwoMatches() {
        String searchinput = "Betriebssysteme Programmierung";
        List<Modul> results = suchService.search(searchinput);

        assertEquals(2, results.size());
    }

    @Test
    void fullTextSearchFindsGermanTitle() {
        String searchinput = "Betriebssysteme";
        List<Modul> results = suchService.search(searchinput);

        assertTrue(results.get(0).getTitelDeutsch().contains(searchinput));
    }

    @Test
    void fullTextSearchFindsEnglishTitle() {
        String searchinput = "Operating";
        List<Modul> results = suchService.search(searchinput);

        assertTrue(results.get(0).getTitelEnglisch().contains(searchinput));
    }

    @Test
    void fullTextSearchfindsInhalteInVeranstaltungsbeschreibung() {
        String searchinput = "Architekturformen";
        List<Modul> results = suchService.search(searchinput);
        Set<Veranstaltung> veranstaltungen = results.get(0).getVeranstaltungen();
        List<Veranstaltung> results2 = new ArrayList<>(veranstaltungen);

        assertTrue(results2.get(0).getBeschreibung().getInhalte().contains(searchinput));
    }

    @Test
    void fullTextSearchFindsFullWordsFromParts() {
        String searchinput = "Betriebs";
        List<Modul> results = suchService.search(searchinput);

        assertTrue(results.get(0).getTitelDeutsch().contains(searchinput));
    }

    @Test
    void fullTextSearchFindsSplittedWords() {
        String searchinput = "Operating systems";
        List<Modul> results = suchService.search(searchinput);

        assertTrue(results.get(0).getTitelEnglisch().contains(searchinput));
    }

    @Test
    void fullTextSearchFindsMultipleWordsFromParts() {
        String searchinput = "Op sys";
        List<Modul> results = suchService.search(searchinput);

        assertEquals(results.get(0).getTitelEnglisch(), completeModul.getTitelEnglisch());
    }

}
