package mops.module.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
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
    private ModulSnapshotRepository modulRepository;

    private Modul fakeModulBetriebssysteme;
    private Modul fakeModulProgrammierung;
    private Modul fakeInvisibleModulProgrammierung;

    @BeforeEach
    void init() {
        fakeModulBetriebssysteme = ModulFaker.generateFakeModul();
        fakeModulBetriebssysteme.setTitelDeutsch("Betriebssysteme");
        fakeModulBetriebssysteme.setTitelEnglisch("Operating systems");

        fakeModulProgrammierung = ModulFaker.generateFakeModul();
        fakeModulProgrammierung.setTitelDeutsch("Programmierung");
        fakeModulProgrammierung.setTitelEnglisch("Programming");

        fakeInvisibleModulProgrammierung = ModulFaker.generateFakeModul();
        fakeInvisibleModulProgrammierung.setTitelDeutsch("Programmierung");
        fakeInvisibleModulProgrammierung.setSichtbar(false);

        modulRepository.save(fakeModulBetriebssysteme);
        modulRepository.save(fakeModulProgrammierung);
        modulRepository.save(fakeInvisibleModulProgrammierung);
    }

    @AfterEach
    void clearDatabase() {
        modulRepository.deleteAll();
    }

    @Test
    void searchTestNoMatchNoResult() {
        String searchinput = "Internet";

        List<Modul> results = suchService.search(searchinput);

        assertTrue(results.isEmpty());
    }

    @Test
    void searchTestOneMatchOneResult() {
        String searchinput = "Betriebssysteme";

        List<Modul> results = suchService.search(searchinput);

        assertEquals(1, results.size());
    }

    @Test
    void searchTestOnlyVisibleModulesAsResults() {
        String searchinput = "Programmierung";

        List<Modul> results = suchService.search(searchinput);

        assertEquals(1, results.size());
    }

    @Test
    void searchTestTwoMatchesTwoResults() {
        String searchinput = "Betriebssysteme Programmierung";

        List<Modul> results = suchService.search(searchinput);

        assertEquals(2, results.size());
    }

    @Test
    void searchTestGermanTitle() {
        String searchinput = "Betriebssysteme";

        List<Modul> results = suchService.search(searchinput);

        assertTrue(results.get(0).getTitelDeutsch().contains(searchinput));
    }

    @Test
    void searchTestEnglishTitle() {
        String searchinput = "Operating";

        List<Modul> results = suchService.search(searchinput);

        assertTrue(results.get(0).getTitelEnglisch().contains(searchinput));
    }

    @Test
    void searchTestVeranstaltungsbeschreibungInhalte() {
        Veranstaltung veranstaltung;
        veranstaltung = fakeModulProgrammierung.getVeranstaltungen().iterator().next();
        String searchinput = veranstaltung.getBeschreibung().getInhalte();

        List<Modul> results = suchService.search(searchinput);

        assertEquals(results.get(0).getTitelDeutsch(), fakeModulProgrammierung.getTitelDeutsch());
    }

    @Test
    void searchTestPartialSearchTerm() {
        String searchinput = "Betriebs";

        List<Modul> results = suchService.search(searchinput);

        assertTrue(results.get(0).getTitelDeutsch().contains(searchinput));
    }

    @Test
    void searchTestMultipleSearchTerms() {
        String searchinput = "Operating systems";

        List<Modul> results = suchService.search(searchinput);

        assertTrue(results.get(0).getTitelEnglisch().contains(searchinput));
    }

    @Test
    void searchTestMultiplePartialSearchTerms() {
        String searchinput = "Op sys";

        List<Modul> results = suchService.search(searchinput);
        String resultTitle = results.get(0).getTitelEnglisch();

        assertEquals(resultTitle, fakeModulBetriebssysteme.getTitelEnglisch());
    }

}
