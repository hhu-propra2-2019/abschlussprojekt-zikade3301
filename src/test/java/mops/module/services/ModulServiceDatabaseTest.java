package mops.module.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

import mops.module.database.Antrag;
import mops.module.database.Modul;
import mops.module.repositories.AntragsRepository;
import mops.module.repositories.ModulSnapshotRepository;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class ModulServiceDatabaseTest {
    private ModulService modulService;
    private JsonService jsonService;

    @Autowired
    private AntragsRepository antragsRepository;

    @Autowired
    private ModulSnapshotRepository modulSnapshotRepository;
    private String modul1, modul2, modul3, modul4, diffs1, diffs2;

    @AfterEach
    public void clear() {
        antragsRepository.deleteAll();
        modulSnapshotRepository.deleteAll();
    }

    @BeforeEach
    public void init() {
        //antragsRepository = mock(AntragsRepository.class);
        //modulSnapshotRepository = mock(ModulSnapshotRepository.class);
        jsonService = new JsonService();
        modulService = new ModulService(antragsRepository, modulSnapshotRepository, jsonService);

        modul1 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3}]," +
                "\"modulkategorie\":\"MASTERARBEIT\"}";
        modul2 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3}]," +
                "\"modulkategorie\":\"BACHELORARBEIT\"}";
        modul3 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3," +
                "\"voraussetzungenTeilnahme\":[{}]}],\"modulkategorie\":\"MASTERARBEIT\"}";
        modul4 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3," +
                "\"voraussetzungenTeilnahme\":[{\"titel\":\"test\"}]}]," +
                "\"modulkategorie\":\"BACHELORARBEIT\"}";
        diffs1 = "{\"id\":5," +
                "\"modulkategorie\":\"BACHELORARBEIT\"}";
        diffs2 = "{\"id\":5,\"veranstaltungen\":[{\"id\":3," +
                "\"voraussetzungenTeilnahme\":[{\"titel\":\"test\"}]}]," +
                "\"modulkategorie\":\"BACHELORARBEIT\"}";
    }

    @Test
    public void addModulCreationAntragTestCreateOne() {
        modulService.addModulCreationAntrag(jsonService.jsonObjectToModul(modul1));
        assertThat(antragsRepository.count()).isEqualTo(1);
    }

    @Test
    public void addModulModificationAntragTestCreateOne() {
        modulService.addModulModificationAntrag(jsonService.jsonObjectToModul(modul1));
        assertThat(antragsRepository.count()).isEqualTo(1);
    }

    @Test
    public void approveModulCreationAntragTestAntragCreated() {
        modulService.addModulCreationAntrag(jsonService.jsonObjectToModul(modul1));
        Antrag antrag = modulService.getAlleAntraege().get(0);
        modulService.approveModulCreationAntrag(antrag);
        assertThat(modulSnapshotRepository.count()).isEqualTo(1);
    }

    @Test
    public void approveModulCreationAntragTestModulIdIsAddedToAntrag() {
        modulService.addModulCreationAntrag(jsonService.jsonObjectToModul(modul1));
        Antrag antrag = modulService.getAlleAntraege().get(0);
        modulService.approveModulCreationAntrag(antrag);
        Modul modul = modulService.getAlleModule().get(0);
        assertThat(antrag.getModulid()).isEqualTo(modul.getId());
    }

//    @Test
//    public void approveModulModificationAntragTest() {
//        modulService.addModulCreationAntrag(jsonService.jsonObjectToModul(modul1));
//        modulService.approveModulCreationAntrag(modulService.getAlleAntraege().get(0));
//        Modul modul = modulService.getAlleModule().get(0);
//
//        Modul aenderungen = jsonService.jsonObjectToModul(diffs1);
//        modulService.addModulModificationAntrag(aenderungen);
//        Antrag antrag = modulService.getAlleAntraege().get(1);
//        modulService.approveModulModificationAntrag(antrag);
//
//        Modul geaendertesModul = modulService.getAlleModule().get(0);
//
//        try {
//            JSONAssert.assertEquals(jsonService.modulToJsonObject(geaendertesModul), modul2, false);
//        } catch (JSONException e) {
//            fail(e.toString());
//        }
//
//    }

}
