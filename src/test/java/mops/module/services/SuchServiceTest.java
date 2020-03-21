package mops.module.services;

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
}