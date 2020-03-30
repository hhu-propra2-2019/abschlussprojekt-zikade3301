package mops.module.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.repositories.AntragRepository;
import mops.module.repositories.ModulSnapshotRepository;
import mops.module.services.AntragService;
import mops.module.services.JsonService;
import mops.module.services.ModulService;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class ModulFakerTest {
    private ModulService modulService;
    private AntragService antragService;
    @Autowired
    private AntragRepository antragRepository;
    @Autowired
    private ModulSnapshotRepository modulSnapshotRepository;
    private CustomComparator ignoreDates;

    /**
     * Erstellt Instanzen von ModulService und AntragService und leert die Testdatenbank
     * vor jedem Test.
     */
    @BeforeEach
    public void init() {
        modulService = new ModulService(antragRepository, modulSnapshotRepository);
        antragService = new AntragService(antragRepository, modulSnapshotRepository);

        antragRepository.deleteAll();
        modulSnapshotRepository.deleteAll();

        ignoreDates = new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("datumErstellung", (o1, o2) -> true),
                new Customization("datumAenderung", (o1, o2) -> true));
    }

    @AfterEach
    public void emptyRepo() {
        antragRepository.deleteAll();
        modulSnapshotRepository.deleteAll();
    }

    @Test
    public void generateFakeModulTest() {
        Modul modul = ModulFaker.generateFakeModul();

        antragService.addModulCreationAntrag(modul, "Beispielantragsteller");
        List<Antrag> antraege = antragService.getAlleAntraege();
        antraege.stream().forEach(antragService::approveModulCreationAntrag);

        Modul repoModule = modulService.getAllModule().get(0);
        assertThat(repoModule).isNotNull();
    }
}
