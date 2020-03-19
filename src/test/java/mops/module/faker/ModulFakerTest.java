package mops.module.faker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.repositories.AntragRepository;
import mops.module.repositories.ModulSnapshotRepository;
import mops.module.services.AntragService;
import mops.module.services.JsonService;
import mops.module.services.ModulService;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mops.module.modulGenerator.ModulFaker;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
public class ModulFakerTest {
    private ModulService modulService;
    private AntragService antragService;
    @Autowired
    private AntragRepository antragRepository;
    @Autowired
    private ModulSnapshotRepository modulSnapshotRepository;
    private CustomComparator ignoreDates;

    @BeforeEach
    public void init() {
        modulService = new ModulService(antragRepository, modulSnapshotRepository);
        antragService = new AntragService(antragRepository, modulSnapshotRepository);
        ignoreDates = new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("datumErstellung", (o1, o2) -> true),
                new Customization("datumAenderung", (o1, o2) -> true));
    }

    @Test
    public void generateFakeModulTest() {
        List<Modul> module = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            module.add(ModulFaker.generateFakeModul());
        }

        module.stream().forEach(antragService::addModulCreationAntrag);
        List<Antrag> antraege = antragService.getAlleAntraege();
        antraege.stream().forEach(antragService::approveModulCreationAntrag);

        List<Modul> repoModule = modulService.getAllModule();
        assertThat(repoModule.size()).isEqualTo(1);

        for(Modul m : repoModule) {
            try {
                JSONAssert.assertEquals(JsonService.modulToJsonObject(m),
                        JsonService.modulToJsonObject(repoModule.get(0)), ignoreDates);
            } catch (JSONException e) {
                fail(e.toString());
            }
        }
    }
}
