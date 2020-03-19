package mops.module;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import mops.module.database.Modul;
import mops.module.repositories.ModulSnapshotRepository;
import mops.module.services.SuchService;
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

    @BeforeEach
    public void init() {

        String title = "Programmierung";
        modul = new Modul();
        modul.setTitelDeutsch(title);
        modul.setTitelEnglisch("english blabla");
    }


    @Test
    void searchReturnsModulObject() {
        modulRepo.save(modul);
        List<Modul> results = suchService.searchForModuleByTitle(modul.getTitelDeutsch());
        assertThat(results.get(0).equals(modul));
    }

    @Test
    void searchReturnsCorrectResultSize() {
        modulRepo.save(modul);
        List<Modul> results = suchService.searchForModuleByTitle(modul.getTitelDeutsch());
        assertThat(results.size() == 1);
    }

    @Test
    void returnedModulContainsSearchterm() {
        modulRepo.save(modul);
        List<Modul> results = suchService.searchForModuleByTitle(modul.getTitelDeutsch());
        assertThat(results.get(0).getTitelDeutsch().equals(modul.getTitelDeutsch()));
    }

    @Test
    void findWordsAlthoughTheyAreInUpperOrLowercase() {
        modulRepo.save(modul);
        List<Modul> results = suchService.searchForModuleByTitle("programmierung");
        assertThat(results.get(0).getTitelDeutsch().equals(modul.getTitelDeutsch()));
    }

    @Test
    void searchForSubstringReturnsResult() {
        modulRepo.save(modul);
        List<Modul> results = suchService.searchForModuleByTitle("prog");
        assertThat(results.size() == 1);
        assertThat(results.get(0).getTitelDeutsch().equals(modul.getTitelDeutsch()));
    }
}