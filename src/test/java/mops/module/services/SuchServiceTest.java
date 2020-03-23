package mops.module.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import mops.module.database.Modul;
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
    SuchService suchService;

    @Autowired
    HibernateModuleSearch hbsearch;

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
        assertEquals(1, results.size());
    }

    @Test
    void returnedModulContainsSearchterm() {
        List<Modul> results = hbsearch.search(modul.getTitelDeutsch());//suchService.searchForModuleByTitle(modul.getTitelDeutsch());
        assertEquals(results.get(0).getTitelDeutsch(), modul.getTitelDeutsch());
    }

    @Test
    void findWordsAlthoughTheyAreInUpperOrLowercase() {
        List<Modul> results = hbsearch.search("programmierung*");
        assertEquals(results.get(0).getTitelDeutsch(), modul.getTitelDeutsch());
    }

    @Test
    void searchForSubstringReturnsResult() {
        List<Modul> results = hbsearch.search("prog*");
        assertEquals(1, results.size());
        assertEquals(results.get(0).getTitelDeutsch(), modul.getTitelDeutsch());
    }

    @Test
    void unsuccessfulSearchReturnsEmptyList() {
        List<Modul> results = hbsearch.search("katze");
        assertTrue(results.isEmpty());
    }

    /*@Test
    @Ignore
    void multiWordQueryReturnsResult() {
        List<Modul> results = hbsearch.search("Graph Entscheidung");
        assertFalse(results.isEmpty());
    }
    */
}