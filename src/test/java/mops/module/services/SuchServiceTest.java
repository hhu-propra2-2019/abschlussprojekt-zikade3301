package mops.module.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mops.module.database.Modul;
import mops.module.database.Veranstaltung;
import mops.module.generator.ModulFaker;
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
        completeModul = ModulFaker.generateFakeModul();
        completeModul.setTitelDeutsch("Betriebssysteme");
        completeModul.setTitelEnglisch("Operating systems");
        Veranstaltung veranstaltung = new ArrayList<>(completeModul.getVeranstaltungen()).get(0);
        Set<Veranstaltung> veranstaltungen = new HashSet<>();
        veranstaltungen.add(veranstaltung);
        completeModul.setVeranstaltungen(veranstaltungen);
        completeModul.refreshMapping();

        anotherModul = ModulFaker.generateFakeModul();
        anotherModul.setTitelDeutsch("Programmierung");
        anotherModul.setTitelEnglisch("Programming");

        modulRepo.save(completeModul);
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
        Veranstaltung veranstaltung = new ArrayList<>(completeModul.getVeranstaltungen()).get(0);
        String searchinput = veranstaltung.getBeschreibung().getInhalte().split(" ")[0];

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
